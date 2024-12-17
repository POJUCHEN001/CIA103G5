package com.cia103g5.user.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.reservation.model.ReservationVO;
import com.cia103g5.user.reservation.service.ReservationServiceSpring;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservations")
public class ReservationViewController {

	@Autowired
	private final ReservationServiceSpring reservationServiceSpring;

	private static final Logger logger = LoggerFactory.getLogger(ReservationViewController.class);

	private SessionMemberDTO getLoggedInMember(HttpSession session) {
		return (SessionMemberDTO) session.getAttribute("loggedInMember");
	}

	public ReservationViewController(ReservationServiceSpring reservationServiceSpring) {
		this.reservationServiceSpring = reservationServiceSpring;
	}

	@GetMapping
	public String getAllReservations(Model model) {
		List<ReservationVO> reservations = reservationServiceSpring.getAllReservations();
		model.addAttribute("reservations", reservations);
		logger.debug("Returning view name: browseReservations");
		return "browseReservations";
	}

	@GetMapping("/select")
	public String showReservationSelectPage(Model model) {
		model.addAttribute("reservations", reservationServiceSpring.getAllReservations());
		return "reservationSelectPage";
	}

	@GetMapping("/search")
	public String searchReservations(@RequestParam("memId") int memId, Model model) {
		List<ReservationVO> reservations = reservationServiceSpring.getReservationsByMemId(memId);
		model.addAttribute("reservations", reservations);
		return "reservationSelectPage";
	}

	@GetMapping("/view/{rsvNo}")
	public String viewReservation(@PathVariable("rsvNo") int rsvNo, Model model) {
		ReservationVO reservation = reservationServiceSpring.getReservationByRsvNo(rsvNo);
		model.addAttribute("reservationVO", reservation);
		return "listOneReservation";
	}

	@GetMapping("/member/query")
	public String showMemberReservationQuery(@RequestParam(value = "rsvNo", required = false) Integer rsvNo,
			HttpSession session, Model model) {
		if (session.getAttribute("isLogin") != null) {
			SessionMemberDTO sessionMember = getLoggedInMember(session);
			Integer memberId = sessionMember.getMemberId();

			// Fetch all reservations for the dropdown list
			List<ReservationVO> allReservations = reservationServiceSpring.getReservationsForMember(memberId);
			model.addAttribute("allReservations", allReservations);

			// Check if a specific search result should be displayed
			if (rsvNo != null) {
				ReservationVO reservation = reservationServiceSpring.getMemberReservationByRsvNo(rsvNo, memberId);
				if (reservation != null) {
					model.addAttribute("reservations", Arrays.asList(reservation)); // Add the searched reservation
				} else {
					model.addAttribute("error", "Reservation not found or you don't have permission to view it.");
				}
			} else {
				model.addAttribute("reservations", allReservations); // Initially display all reservations
			}

			return "member-reservation-query";
		}
		return "redirect:/login";
	}

	@GetMapping("/ft/query")
	public String showFtReservationQuery(@RequestParam(value = "rsvNo", required = false) Integer rsvNo,
			HttpSession session, Model model) {
		if (session.getAttribute("isLogin") != null) {
			Integer ftId = (Integer) session.getAttribute("ftId");

			if (ftId != null) {
				// Fetch all reservations for the dropdown list
				List<ReservationVO> allReservations = reservationServiceSpring.getReservationsForFortuneTeller(ftId);
				model.addAttribute("allReservations", allReservations);

				// Check if a specific search result should be displayed
				if (rsvNo != null) {
					ReservationVO reservation = reservationServiceSpring.getFtReservationByRsvNo(rsvNo, ftId);
					if (reservation != null) {
						model.addAttribute("reservations", Arrays.asList(reservation)); // Add the searched reservation
					} else {
						model.addAttribute("error", "Reservation not found or you don't have permission to view it.");
					}
				} else {
					model.addAttribute("reservations", allReservations); // Initially display all reservations
				}

				return "ft-reservation-query";
			}
		}
		return "redirect:/login";
	}

	@GetMapping("/member/search")
	public String memberSearchReservation(@RequestParam("rsvNo") int rsvNo, Model model, HttpSession session,
			HttpServletRequest request) {
		if (session.getAttribute("isLogin") != null) {
			SessionMemberDTO sessionMember = getLoggedInMember(session);
			Integer memberId = sessionMember.getMemberId();

			// Fetch the specific reservation by ID
			ReservationVO reservation = reservationServiceSpring.getMemberReservationByRsvNo(rsvNo, memberId);

			// Fetch all reservations for the dropdown list
			List<ReservationVO> allReservations = reservationServiceSpring.getReservationsForMember(memberId);
			model.addAttribute("allReservations", allReservations);

			if (reservation != null) {
				List<ReservationVO> reservationList = Arrays.asList(reservation);
				model.addAttribute("reservations", reservationList); // Add the searched reservation
				return "redirect:/reservations/member/query?rsvNo=" + rsvNo; // Redirect back to the query page with the
																				// search result
			} else {
				model.addAttribute("error", "Reservation not found or you don't have permission to view it.");
			}

			return "member-reservation-query";
		}
		String redirectUrl = request.getRequestURL().toString();
		if (request.getQueryString() != null) {
			redirectUrl += "?" + request.getQueryString();
		}
		session.setAttribute("redirectUrl", redirectUrl);
		return "redirect:/login";
	}

	@GetMapping("/ft/search")
	public String ftSearchReservation(@RequestParam("rsvNo") int rsvNo, Model model, HttpSession session,
			HttpServletRequest request) {
		if (session.getAttribute("isLogin") != null) {
			Integer ftId = (Integer) session.getAttribute("ftId");

			if (ftId != null) {
				ReservationVO reservation = reservationServiceSpring.getFtReservationByRsvNo(rsvNo, ftId);
				model.addAttribute("allReservation", reservation);
				if (reservation != null) {
					List<ReservationVO> reservationList = Arrays.asList(reservation);

					model.addAttribute("reservations", reservationList);
					return "redirect:/reservations/ft/query?rsvNo=" + rsvNo;
				} else {
					model.addAttribute("error", "Reservation not found or you don't have permission to view it.");
				}

				return "ft-reservation-query";
			}
		}
		String redirectUrl = request.getRequestURL().toString();
		if (request.getQueryString() != null) {
			redirectUrl += "?" + request.getQueryString();
		}
		session.setAttribute("redirectUrl", redirectUrl);
		return "redirect:/login";
	}

	@GetMapping("/member/statistics")
	public String showMemberReservationStatistics(Model model, HttpSession session) {
		if (session.getAttribute("isLogin") != null) {
			SessionMemberDTO sessionMember = getLoggedInMember(session);
			Integer memberId = sessionMember.getMemberId();

			Map<String, Object> statistics = reservationServiceSpring.getMemberReservationStatistics(memberId);

			model.addAttribute("totalReservations", statistics.get("totalReservations"));
			model.addAttribute("completedReservations", statistics.get("completedReservations"));
			model.addAttribute("cancelledReservations", statistics.get("cancelledReservations"));
			model.addAttribute("pendingReservations", statistics.get("pendingReservations"));
			model.addAttribute("pendingComReservations", statistics.get("pendingComReservations"));
			model.addAttribute("totalSpent", statistics.get("totalSpent"));
			model.addAttribute("skillLabels", statistics.get("skillLabels"));
			model.addAttribute("skillCounts", statistics.get("skillCounts"));
			model.addAttribute("ratingCounts", statistics.get("ratingCounts"));

			model.addAttribute("statistics", statistics);

//            model.addAttribute("memberName", sessionMember.getName());

			return "member-reservation-statistics";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/ft/statistics")
	public String showFtReservationStatistics(Model model, HttpSession session) {
		if (session.getAttribute("isLogin") != null) {
			Integer ftId = (Integer) session.getAttribute("ftId");

			if (ftId != null) {
				// Get reservation statistics for the fortune teller
				Map<String, Object> statistics = reservationServiceSpring.getFtReservationStatistics(ftId);

				// Add all statistics to the model to be used in the Thymeleaf view
				
				model.addAttribute("totalReservations", statistics.get("totalReservations"));
				model.addAttribute("completedReservations", statistics.get("completedReservations"));
				model.addAttribute("cancelledReservations", statistics.get("cancelledReservations"));
				model.addAttribute("pendingReservations", statistics.get("pendingReservations"));
				model.addAttribute("pendingComReservations", statistics.get("pendingComReservations"));

				model.addAttribute("totalEarnings", statistics.get("totalEarnings"));
				model.addAttribute("skillLabels", statistics.get("skillLabels"));
				model.addAttribute("skillCounts", statistics.get("skillCounts"));
				model.addAttribute("ratingCounts", statistics.get("ratingCounts"));

				model.addAttribute("statistics", statistics);

				return "ft-reservation-statistics";
			}
		}
		return "redirect:/login";
	}

	@GetMapping("/member/financial")
	public String showMemberFinancialManagement(Model model, HttpSession session) {
		if (session.getAttribute("isLogin") != null) {
			SessionMemberDTO sessionMember = getLoggedInMember(session);
			Integer memberId = sessionMember.getMemberId();
			Map<String, Object> financialData = reservationServiceSpring.getMemberFinancialData(memberId);
			model.addAttribute("financialData", financialData);
			return "member-financial-management";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/ft/financial")
	public String showFtFinancialManagement(Model model, HttpSession session) {
		if (session.getAttribute("isLogin") != null) {
			Integer ftId = (Integer) session.getAttribute("ftId");

			if (ftId != null) {
				Map<String, Object> financialData = reservationServiceSpring.getFtFinancialData(ftId);

				// Add all financial data to the model
				model.addAttribute("financialData", financialData);

				// Explicitly add individual attributes for easier access in the view
				model.addAttribute("bankAccount", financialData.get("bankAccount"));
				model.addAttribute("monthlyEarnings", financialData.get("monthlyEarnings"));
				model.addAttribute("yearlyEarnings", financialData.get("yearlyEarnings"));
				model.addAttribute("monthLabels", financialData.get("monthLabels"));
				model.addAttribute("monthlyEarningsData", financialData.get("monthlyEarningsData"));
				model.addAttribute("transactions", financialData.get("transactions"));

				return "ft-financial-management";
			}
		}
		return "redirect:/login";
	}

	@GetMapping("/member/manage")
	public String manageMemberReservations(Model model, HttpSession session) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

		if (isLogin != null && loggedInMember != null) {
			Integer memberId = (Integer) session.getAttribute("memberId");
			if (memberId != null) {
				List<ReservationVO> reservations = reservationServiceSpring.getReservationsForMember(memberId);
				model.addAttribute("reservations", reservations);
				return "member-reservation-management";
			}
		}
		return "redirect:/login";
	}

	@GetMapping("/ft/manage")
	public String manageFtReservations(Model model, HttpSession session) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

		if (isLogin != null && loggedInMember != null) {
			Integer ftId = (Integer) session.getAttribute("ftId");
			if (ftId != null) {
				List<ReservationVO> reservations = reservationServiceSpring.getReservationsForFortuneTeller(ftId);
				model.addAttribute("reservations", reservations);
				return "ft-reservation-management";
			}
		}
		return "redirect:/login";
	}
	
	@PostMapping("/confirm")
    public String confirmReservation(@RequestParam("rsvNo") Integer rsvNo, RedirectAttributes redirectAttributes) {
        try {
            reservationServiceSpring.confirmReservation(rsvNo);
            redirectAttributes.addFlashAttribute("message", "預約已確認");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "確認預約失敗: " + e.getMessage());
        }
        return "redirect:/reservations/ft/manage";
    }

    @PostMapping("/reject")
    public String rejectReservation(@RequestParam("rsvNo") Integer rsvNo, RedirectAttributes redirectAttributes) {
        try {
            reservationServiceSpring.rejectReservation(rsvNo);
            redirectAttributes.addFlashAttribute("message", "預約已拒絕");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "拒絕預約失敗: " + e.getMessage());
        }
        return "redirect:/reservations/ft/manage";
    }

	@PostMapping("/update")
	public String updateReservation(@RequestParam Integer rsvNo, @RequestParam String action, HttpSession session) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

		if (isLogin != null && loggedInMember != null) {
			Integer ftId = (Integer) session.getAttribute("ftId");
			if (ftId != null) {
				if ("confirm".equals(action)) {
					reservationServiceSpring.confirmReservation(rsvNo);
				} else if ("reject".equals(action)) {
					reservationServiceSpring.rejectReservation(rsvNo);
				}
				return "redirect:/reservations/ft/manage";
			}
		}
		return "redirect:/login";
	}

	@PostMapping("/cancel")
	public String cancelReservation(@RequestParam Integer rsvNo, HttpSession session) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

		if (isLogin != null && loggedInMember != null) {
			Integer memberId = (Integer) session.getAttribute("memberId");
			if (memberId != null) {
				reservationServiceSpring.cancelReservation(rsvNo);
				return "redirect:/reservations/member/manage";
			}
		}
		return "redirect:/login";
	}

	@GetMapping("/rate/{id}")
	public String showRatingForm(@PathVariable Integer id, Model model, HttpSession session) {
	    Object isLogin = session.getAttribute("isLogin");
	    SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

	    if (isLogin != null && loggedInMember != null) {
	        Integer memberId = (Integer) session.getAttribute("memberId");
	        if (memberId != null) {
	            ReservationVO reservation = reservationServiceSpring.getReservationByRsvNo(id);
	            model.addAttribute("reservation", reservation);
	            return "rate-reservation";
	        }
	    }
	    return "redirect:/login";
	}

	@PostMapping("/rate/{id}")
	public String submitRating(@PathVariable Integer id, @RequestParam("rating") Integer rating,
	        @RequestParam("ratingContent") String ratingContent, HttpSession session,
	        RedirectAttributes redirectAttributes) {
	    Object isLogin = session.getAttribute("isLogin");
	    SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

	    if (isLogin != null && loggedInMember != null) {
	        Integer memberId = (Integer) session.getAttribute("memberId");
	        if (memberId != null) {
	            try {
	                reservationServiceSpring.submitRating(id, memberId, rating, ratingContent);
	                redirectAttributes.addFlashAttribute("message", "Rating submitted successfully");
	                return "redirect:/reservations/member/manage";
	            } catch (Exception e) {
	                redirectAttributes.addFlashAttribute("error", "Failed to submit rating: " + e.getMessage());
	                return "redirect:/reservations/rate/" + id;
	            }
	        }
	    }
	    return "redirect:/login";
	}

	@GetMapping("/feedback/{id}")
	public String showFeedbackForm(@PathVariable Integer id, Model model, HttpSession session) {
	    Object isLogin = session.getAttribute("isLogin");
	    SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

	    if (isLogin != null && loggedInMember != null) {
	        Integer ftId = (Integer) session.getAttribute("ftId");
	        if (ftId != null) {
	            ReservationVO reservation = reservationServiceSpring.getReservationByRsvNo(id);
	            model.addAttribute("reservation", reservation);
	            return "feedback-reservation";
	        }
	    }
	    return "redirect:/login";
	}

	@PostMapping("/feedback/{id}")
	public String submitFeedback(@PathVariable Integer id, 
	                           @RequestParam("feedback") String feedback,
	                           @RequestParam(value = "note", required = false) String note,
	                           HttpSession session,
	                           RedirectAttributes redirectAttributes) {
	    Object isLogin = session.getAttribute("isLogin");
	    SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

	    if (isLogin != null && loggedInMember != null) {
	        Integer ftId = (Integer) session.getAttribute("ftId");
	        if (ftId != null) {
	            try {
	                reservationServiceSpring.submitFeedback(id, ftId, feedback, note);
	                redirectAttributes.addFlashAttribute("message", "回饋和備註已成功提交");
	                return "redirect:/reservations/ft/manage";
	            } catch (Exception e) {
	                redirectAttributes.addFlashAttribute("error", "提交回饋和備註失敗: " + e.getMessage());
	                return "redirect:/reservations/feedback/" + id;
	            }
	        }
	    }
	    return "redirect:/login";
	}




//	@GetMapping("/admin")
//	public String adminManageReservations(Model model, HttpSession session) {
//		if (session.getAttribute("adminId") == null) {
//			return "redirect:/login";
//		}
//		List<ReservationVO> allReservations = reservationServiceSpring.getAllReservations();
//		model.addAttribute("reservations", allReservations);
//		return "/admin-reservation-management";
//	}
//
//	@PostMapping("/admin/add")
//	public String adminAddReservation(@ModelAttribute ReservationVO reservation, RedirectAttributes redirectAttributes,
//			HttpSession session) {
//		if (session.getAttribute("adminId") == null) {
//			return "redirect:/login";
//		}
//		try {
//			reservationServiceSpring.addReservation(reservation);
//			redirectAttributes.addFlashAttribute("message", "Reservation added successfully");
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("error", "Error adding reservation: " + e.getMessage());
//		}
//		return "redirect:/reservations/admin";
//	}
//
//	@PostMapping("/admin/edit")
//	public String adminEditReservation(@ModelAttribute ReservationVO reservation, RedirectAttributes redirectAttributes,
//			HttpSession session) {
//		if (session.getAttribute("adminId") == null) {
//			return "redirect:/login";
//		}
//		try {
//			reservationServiceSpring.updateReservation(reservation);
//			redirectAttributes.addFlashAttribute("message", "Reservation updated successfully");
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("error", "Error updating reservation: " + e.getMessage());
//		}
//		return "redirect:/reservations/admin";
//	}
//
//	@PostMapping("/admin/delete")
//	public String adminDeleteReservation(@RequestParam("rsvNo") int rsvNo, RedirectAttributes redirectAttributes,
//			HttpSession session) {
//		if (session.getAttribute("adminId") == null) {
//			return "redirect:/login";
//		}
//		try {
//			reservationServiceSpring.deleteReservation(rsvNo);
//			redirectAttributes.addFlashAttribute("message", "Reservation deleted successfully");
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("error", "Error deleting reservation: " + e.getMessage());
//		}
//		return "redirect:/reservations/admin";
//	}

}
