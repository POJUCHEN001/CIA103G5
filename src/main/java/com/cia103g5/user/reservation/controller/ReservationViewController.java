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
	public String showMemberReservationQuery(HttpSession session, Model model) {

		if (session.getAttribute("isLogin") != null) {

			SessionMemberDTO sessionMember = getLoggedInMember(session);
			Integer memberId = (Integer) sessionMember.getMemberId();
			List<ReservationVO> reservations = reservationServiceSpring.getReservationsForMember(memberId);
			model.addAttribute("reservations", reservations);
			return "member-reservation-query";
		} else
			return "redirect:/login";
	}

//	 @GetMapping("/ft/query")
//	    public String showFtReservationQuery(HttpSession session) {
//	        if (getLoggedInMember(session) == null) {
//	            return "redirect:/login";
//	        }
//	        return "ft-reservation-query";
//	    }

	@GetMapping("/member/statistics")
	public String showMemberReservationStatistics(Model model, HttpSession session) {
		if (session.getAttribute("isLogin") != null) {
			SessionMemberDTO sessionMember = getLoggedInMember(session);
			Integer memberId = sessionMember.getMemberId();

			Map<String, Object> statistics = reservationServiceSpring.getMemberReservationStatistics(memberId);

			model.addAttribute("completedReservations", statistics.get("completedReservations"));
			model.addAttribute("cancelledReservations", statistics.get("cancelledReservations"));
			model.addAttribute("pendingReservations", statistics.get("pendingReservations"));
			model.addAttribute("totalSpent", statistics.get("totalSpent"));
			model.addAttribute("skillLabels", statistics.get("skillLabels"));
			model.addAttribute("skillCounts", statistics.get("skillCounts"));

			model.addAttribute("statistics", statistics);

//            model.addAttribute("memberName", sessionMember.getName());

			return "member-reservation-statistics";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/ft/statistics")
	public String showFtReservationStatistics(Model model, HttpSession session) {
		// Check if the user is logged in and if the session has the fortune teller ID
		Integer ftId = (Integer) session.getAttribute("ftId");

		if (ftId == null) {
			// If no ftId is found in the session, redirect to login page
			return "redirect:/login";
		}

		// Get reservation statistics for the fortune teller
		Map<String, Object> statistics = reservationServiceSpring.getFtReservationStatistics(ftId);

		// Assuming statistics is a map with necessary values
		Integer completedReservations = (Integer) statistics.get("completedReservations");
		Integer cancelledReservations = (Integer) statistics.get("cancelledReservations");
		Integer pendingReservations = (Integer) statistics.get("pendingReservations");
//	    List<String> skillLabels = (List<String>) statistics.get("skillLabels");
//	    List<Integer> skillCounts = (List<Integer>) statistics.get("skillCounts");

		// Add all statistics to the model to be used in the Thymeleaf view
		model.addAttribute("completedReservations", completedReservations);
		model.addAttribute("cancelledReservations", cancelledReservations);
		model.addAttribute("pendingReservations", pendingReservations);
//	    model.addAttribute("skillLabels", skillLabels);
//	    model.addAttribute("skillCounts", skillCounts);

		model.addAttribute("statistics", statistics);

		return "/ft-reservation-statistics";
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
		Integer ftId = (Integer) session.getAttribute("ftId");
		if (ftId == null) {
			return "redirect:/login";
		}
		Map<String, Object> financialData = reservationServiceSpring.getFtFinancialData(ftId);
		model.addAttribute("financialData", financialData);
		return "/ft-financial-management";
	}

	@GetMapping("/manage")
	public String manageReservations(Model model, HttpSession session) {
		if (session.getAttribute("isLogin") != null) {
			Integer memberId = (Integer) session.getAttribute("memberId");
			Integer ftId = (Integer) session.getAttribute("ftId");

			List<ReservationVO> reservations;

			if (memberId != null && ftId == null) {
				// For regular members
				reservations = reservationServiceSpring.getReservationsForMember(memberId);
				model.addAttribute("reservations", reservations);
				return "member-reservation-management";
			} else if (memberId != null && ftId != null) {
				// For fortune tellers
				reservations = reservationServiceSpring.getReservationsForFortuneTeller(ftId);
				model.addAttribute("reservations", reservations);
				return "ft-reservation-management";
			}
		}
		return "redirect:/login";
	}

//	@GetMapping("/manage")
//	public String manageReservations(Model model, HttpSession session) {
//		Integer memberId = (Integer) session.getAttribute("memberId");
//		Integer ftId = (Integer) session.getAttribute("ftId");
//
//		List<ReservationVO> reservations;
//		if (memberId != null) {
//			reservations = reservationServiceSpring.getReservationsForMember(memberId);
//			model.addAttribute("userRole", "member");
//			model.addAttribute("reservations", reservations);
//			return "member-reservation-management";
//		} else if (ftId != null) {
//			reservations = reservationServiceSpring.getReservationsForFortuneTeller(ftId);
//			model.addAttribute("userRole", "fortuneTeller");
//			model.addAttribute("reservations", reservations);
//			return "ft-reservation-management";
//		} else {
//			return "/redirect:/login";
//		}
//	}

	@GetMapping("/member/search")
	public String memberSearchReservation(@RequestParam("rsvNo") int rsvNo, Model model, HttpSession session,
			HttpServletRequest request) {
		if (session.getAttribute("isLogin") != null) {
			SessionMemberDTO sessionMember = getLoggedInMember(session);
			Integer memberId = sessionMember.getMemberId();

			ReservationVO reservations = reservationServiceSpring.getMemberReservationByRsvNo(rsvNo, memberId);

			if (reservations != null) {
				model.addAttribute("reservations", reservations);
			} else {
				model.addAttribute("error", "Reservation not found or you don't have permission to view it.");
			}

			return "member-reservation-query";
		} else {
			String redirectUrl = request.getRequestURL().toString();
			if (request.getQueryString() != null) {
				redirectUrl += "?" + request.getQueryString();
			}
			session.setAttribute("redirectUrl", redirectUrl);
			return "redirect:/login";
		}
	}

	@GetMapping("/ft/search")
	public String ftSearchReservation(@RequestParam("rsvNo") int rsvNo, Model model, HttpSession session,
			HttpServletRequest request) {
		Integer ftId = (Integer) session.getAttribute("ftId");

		if (ftId == null) {
			String redirectUrl = request.getRequestURL().toString();
			if (request.getQueryString() != null) {
				redirectUrl += "?" + request.getQueryString();
			}
			session.setAttribute("redirectUrl", redirectUrl);
			return "redirect:/login";
		}

		ReservationVO reservation = reservationServiceSpring.getFtReservationByRsvNo(rsvNo, ftId);

		if (reservation != null) {
			model.addAttribute("reservation", reservation);
		} else {
			model.addAttribute("error", "Reservation not found or you don't have permission to view it.");
		}

		return "/ft-reservation-query";
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
