package com.cia103g5.user.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.reservation.model.ReservationVO;
import com.cia103g5.user.reservation.repository.ReservationRepository;

import com.cia103g5.user.member.model.MemberRepository;
import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.availabletime.model.AvailableTimeRepository;
import com.cia103g5.user.availabletime.model.AvailableTimeVO;
import com.cia103g5.user.ft.model.FtRepository;
import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.ftskill.model.FtSkillVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceSpring {

	private static final Logger logger = LoggerFactory.getLogger(ReservationServiceSpring.class);

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private AvailableTimeRepository availableTimeRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private FtRepository ftRepository;

	public MemberVO getMemberVO(int memberId) {
		return memberRepository.findById(memberId).orElse(null);
	}

	public ReservationServiceSpring(MemberRepository memberRepository, FtRepository ftRepository,
			ReservationRepository reservationRepository) {
		this.memberRepository = memberRepository;
		this.ftRepository = ftRepository;
		this.reservationRepository = reservationRepository;
	}

	public List<ReservationVO> getReservationsForMember(Integer memberId) {
		MemberVO member = memberRepository.findById(memberId)
				.orElseThrow(() -> new RuntimeException("Member not found"));
		return reservationRepository.findByMemberId(member);
	}

	public List<ReservationVO> getReservationsForFortuneTeller(Integer ftId) {
		FtVO fortuneTeller = ftRepository.findById(ftId)
				.orElseThrow(() -> new RuntimeException("Fortune Teller not found"));
		return reservationRepository.findByFtId(fortuneTeller);
	}

	public void cancelReservation(int rsvNo) {
		ReservationVO reservation = getReservationByRsvNo(rsvNo);
		if (reservation != null) {
			reservation.setRsvStatus((byte) 2); // Assuming 2 is the status for cancelled
			reservationRepository.save(reservation);
		} else {
			throw new RuntimeException("Reservation not found");
		}
	}

	public List<ReservationVO> getAllReservations() {
		return reservationRepository.findAll();
	}

	public ReservationVO getReservationByRsvNo(int rsvNo) {
		logger.debug("Attempting to retrieve reservation with number: " + rsvNo);
		Optional<ReservationVO> reservationOpt = reservationRepository.findByRsvNo(rsvNo);
		if (reservationOpt.isPresent()) {
			ReservationVO reservation = reservationOpt.get();
			logger.debug("Reservation found: " + reservation.getRsvNo());
			return reservation;
		}
		logger.error("No reservation found for rsvNo: " + rsvNo);
		return null;
	}

	public ReservationVO addReservation(ReservationVO reservation) {
		return reservationRepository.save(reservation);
	}

	public ReservationVO addReservation(MemberVO memberId, FtVO ftId, AvailableTimeVO availableTimeNo,
			FtSkillVO skillNo, Byte rsvStatus, Integer price, Byte payment, Integer rating, String ratingContent,
			String ftFeedback, String note) {
		ReservationVO reservation = new ReservationVO();
		reservation.setMemberId(memberId);
		reservation.setFtId(ftId);
		reservation.setAvailableTimeNo(availableTimeNo);
		reservation.setSkillNo(skillNo);
		reservation.setRsvStatus(rsvStatus);
		reservation.setPrice(price);
		reservation.setPayment(payment);
		reservation.setRating(rating);
		reservation.setRatingContent(ratingContent);
		reservation.setFtFeedback(ftFeedback);
		reservation.setNote(note);
		return reservationRepository.save(reservation);
	}

	public ReservationVO updateReservation(ReservationVO reservation) {
		if (reservationRepository.existsById(reservation.getRsvNo())) {
			return reservationRepository.save(reservation);
		} else {
			throw new RuntimeException("Reservation not found with id: " + reservation.getRsvNo());
		}
	}

	public ReservationVO updateReservation(int rsvNo, MemberVO memberId, FtVO ftId, AvailableTimeVO availableTimeNo,
			FtSkillVO skillNo, Byte rsvStatus, Integer price, Byte payment, Integer rating, String ratingContent,
			String ftFeedback, String note) {
		ReservationVO reservation = getReservationByRsvNo(rsvNo);
		if (reservation != null) {
			reservation.setMemberId(memberId);
			reservation.setFtId(ftId);
			reservation.setAvailableTimeNo(availableTimeNo);
			reservation.setSkillNo(skillNo);
			reservation.setRsvStatus(rsvStatus);
			reservation.setPrice(price);
			reservation.setPayment(payment);
			reservation.setRating(rating);
			reservation.setRatingContent(ratingContent);
			reservation.setFtFeedback(ftFeedback);
			reservation.setNote(note);
			return reservationRepository.save(reservation);
		}
		throw new RuntimeException("Reservation not found");
	}

	public void deleteReservation(int rsvNo) {
		reservationRepository.deleteById(rsvNo);
	}

	public List<ReservationVO> getReservationsByMemId(Integer memberId) {
		Optional<MemberVO> member = memberRepository.findById(memberId);
		if (member.isPresent()) {
			return reservationRepository.findByMemberId(member.get());
		} else {
			return Collections.emptyList(); // or handle the case when the member is not found
		}
	}

	public List<ReservationVO> getReservationsByFtId(Integer ftId) {
		Optional<FtVO> fortuneTeller = ftRepository.findById(ftId);
		if (fortuneTeller.isPresent()) {
			return reservationRepository.findByFtId(fortuneTeller.get());
		} else {
			return Collections.emptyList(); // or handle the case when the fortune teller is not found
		}
	}

	public List<ReservationVO> getReservationsBySkillNo(FtSkillVO skillNo) {
		return reservationRepository.findBySkillNo(skillNo);
	}

	public Map<String, Object> getMemberReservationStatistics(Integer memberId) {
		List<ReservationVO> reservations = getReservationsForMember(memberId);
		Map<String, Object> statistics = new HashMap<>();

		LocalDateTime now = LocalDateTime.now();

		statistics.put("totalReservations", reservations.size());
//		statistics.put("completedReservations", reservations.stream().filter(r -> r.getRsvStatus() == 1).count());
		statistics.put("completedReservations", reservations.stream()
				.filter(r -> r.getRsvStatus() == 1 && r.getAvailableTimeNo().getEndTime().isBefore(now)).count());
		statistics.put("cancelledReservations", reservations.stream().filter(r -> r.getRsvStatus() == 2).count());
		statistics.put("pendingReservations", reservations.stream().filter(r -> r.getRsvStatus() == 0).count());

		statistics.put("pendingComReservations", reservations.stream().filter(
				r -> r.getRsvStatus() == 1 && r.getPayment() == 1 && r.getAvailableTimeNo().getEndTime().isAfter(now))
				.count());

		statistics.put("totalSpent", reservations.stream().mapToInt(ReservationVO::getPrice).sum());

		Map<String, Long> reservationsPerSkill = reservations.stream()
				.collect(Collectors.groupingBy(r -> r.getSkillNo().getSkillName(), Collectors.counting()));
		statistics.put("reservationsPerSkill", reservationsPerSkill);
		// Add more statistics as needed

		List<String> skillLabels = new ArrayList<>(reservationsPerSkill.keySet());
		List<Long> skillCounts = new ArrayList<>(reservationsPerSkill.values());
		statistics.put("skillLabels", skillLabels);
		statistics.put("skillCounts", skillCounts);

		return statistics;
	}

	public Map<String, Object> getFtReservationStatistics(Integer ftId) {
		List<ReservationVO> reservations = getReservationsForFortuneTeller(ftId);
		Map<String, Object> statistics = new HashMap<>();

		LocalDateTime now = LocalDateTime.now();

		statistics.put("totalReservations", reservations.size());
//		statistics.put("completedReservations", reservations.stream().filter(r -> r.getRsvStatus() == 1).count());

		statistics.put("completedReservations", reservations.stream()
				.filter(r -> r.getRsvStatus() == 1 && r.getAvailableTimeNo().getEndTime().isBefore(now)).count());

		statistics.put("cancelledReservations", reservations.stream().filter(r -> r.getRsvStatus() == 2).count());
		statistics.put("pendingReservations", reservations.stream().filter(r -> r.getRsvStatus() == 0).count());

		statistics.put("pendingComReservations", reservations.stream().filter(
				r -> r.getRsvStatus() == 1 && r.getPayment() == 1 && r.getAvailableTimeNo().getEndTime().isAfter(now))
				.count());

		double totalEarnings = reservations.stream().filter(r -> r.getRsvStatus() == 1)
				.mapToDouble(r -> r.getPrice() - (r.getPrice() * 0.05)).sum();
		statistics.put("totalEarnings", totalEarnings);

		Map<Integer, Long> ratingCounts = reservations.stream().filter(r -> r.getRating() != null)
				.collect(Collectors.groupingBy(ReservationVO::getRating, Collectors.counting()));
		statistics.put("ratingCounts", ratingCounts);
		Map<String, Long> reservationsPerSkill = reservations.stream()
				.filter(r -> r.getSkillNo() != null && r.getSkillNo().getSkillName() != null)
				.collect(Collectors.groupingBy(r -> r.getSkillNo().getSkillName(), Collectors.counting()));
		statistics.put("reservationsPerSkill", reservationsPerSkill);

		List<String> skillLabels = new ArrayList<>(reservationsPerSkill.keySet());
		List<Long> skillCounts = new ArrayList<>(reservationsPerSkill.values());
		statistics.put("skillLabels", skillLabels);
		statistics.put("skillCounts", skillCounts);

		return statistics;
	}

	public Map<String, Object> getMemberFinancialData(Integer memberId) {
		List<ReservationVO> reservations = getReservationsForMember(memberId);
		Map<String, Object> financialData = new HashMap<>();

		// Get bank account (assuming it's stored in the member information)
		MemberVO member = getMemberVO(memberId);
		financialData.put("bankAccount", member.getBankAccount());

		// Filter paid reservations
		List<ReservationVO> paidReservations = reservations.stream().filter(r -> r.getPayment() == 1)
				.collect(Collectors.toList());

		// Calculate monthly spending
		LocalDateTime now = LocalDateTime.now();
		int monthlySpending = paidReservations.stream().filter(
				r -> r.getPaymentTime().getMonth() == now.getMonth() && r.getPaymentTime().getYear() == now.getYear())
				.mapToInt(ReservationVO::getPrice).sum();
		financialData.put("monthlySpending", monthlySpending);

		// Calculate yearly spending
		int yearlySpending = paidReservations.stream().filter(r -> r.getPaymentTime().getYear() == now.getYear())
				.mapToInt(ReservationVO::getPrice).sum();
		financialData.put("yearlySpending", yearlySpending);

		// Get transaction details (only for paid reservations)
		List<Map<String, Object>> transactions = paidReservations.stream().map(r -> {
			Map<String, Object> transaction = new HashMap<>();
			transaction.put("date", r.getPaymentTime());
			transaction.put("amount", r.getPrice());
			transaction.put("fortuneTeller", r.getFtId().getNickname());
			return transaction;
		}).collect(Collectors.toList());
		financialData.put("transactions", transactions);

		return financialData;

	}

	public Map<String, Object> getFtFinancialData(Integer ftId) {
		List<ReservationVO> reservations = getReservationsForFortuneTeller(ftId);
		Map<String, Object> financialData = new HashMap<>();

		// Get fortune teller information
		FtVO fortuneTeller = ftRepository.findById(ftId)
				.orElseThrow(() -> new RuntimeException("Fortune Teller not found"));
		financialData.put("bankAccount", fortuneTeller.getBankAccount());

		// Filter paid reservations
		List<ReservationVO> paidReservations = reservations.stream().filter(r -> r.getPayment() == 1)
				.collect(Collectors.toList());

		// Calculate monthly earnings with 5% platform fee
		LocalDateTime now = LocalDateTime.now();
		double monthlyEarnings = paidReservations.stream()
				.filter(r -> r.getPaymentTime().getMonth() == now.getMonth()
						&& r.getPaymentTime().getYear() == now.getYear())
				.mapToDouble(r -> r.getPrice() * 0.95) // Apply 5% platform fee
				.sum();
		financialData.put("monthlyEarnings", monthlyEarnings);

		// Calculate yearly earnings with 5% platform fee
		double yearlyEarnings = paidReservations.stream().filter(r -> r.getPaymentTime().getYear() == now.getYear())
				.mapToDouble(r -> r.getPrice() * 0.95) // Apply 5% platform fee
				.sum();
		financialData.put("yearlyEarnings", yearlyEarnings);

		// Get monthly labels and data for chart (last 12 months)
		List<String> monthLabels = new ArrayList<>();
		List<Double> monthlyEarningsData = new ArrayList<>();

		for (int i = 11; i >= 0; i--) {
			LocalDateTime month = now.minusMonths(i);
			monthLabels.add(month.format(DateTimeFormatter.ofPattern("yyyy-MM")));

			double monthEarnings = paidReservations.stream()
					.filter(r -> r.getPaymentTime().getMonth() == month.getMonth()
							&& r.getPaymentTime().getYear() == month.getYear())
					.mapToDouble(r -> r.getPrice() * 0.95).sum();
			monthlyEarningsData.add(monthEarnings);
		}

		financialData.put("monthLabels", monthLabels);
		financialData.put("monthlyEarningsData", monthlyEarningsData);

		// Get transaction details (only for paid reservations)
		List<Map<String, Object>> transactions = paidReservations.stream().map(r -> {
			Map<String, Object> transaction = new HashMap<>();
			transaction.put("date", r.getPaymentTime());
			transaction.put("type", "收入");
			double actualEarning = r.getPrice() * 0.95;
			transaction.put("amount", actualEarning);
			transaction.put("note", "來自會員 " + r.getMemberId().getName() + " 的預約");
			return transaction;
		}).collect(Collectors.toList());
		financialData.put("transactions", transactions);

		return financialData;
	}

	@Transactional
	public void confirmReservation(Integer rsvNo) {
		ReservationVO reservation = reservationRepository.findById(rsvNo)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		reservation.setRsvStatus((byte) 1); // Assuming 1 is the status for confirmed
		reservationRepository.save(reservation);
	}

	@Transactional
	public void rejectReservation(Integer rsvNo) {
		ReservationVO reservation = reservationRepository.findById(rsvNo)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		reservation.setRsvStatus((byte) 2); // Assuming 2 is the status for rejected
		reservationRepository.save(reservation);
	}

	@Transactional
	public void submitRating(Integer rsvNo, Integer memberId, Integer rating, String ratingContent) {
		ReservationVO reservation = reservationRepository.findById(rsvNo)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));

		if (!reservation.getMemberId().getMemberId().equals(memberId)) {
			throw new RuntimeException("您無權為此預約提供評分");
		}

		LocalDateTime now = LocalDateTime.now();
		if (reservation.getAvailableTimeNo().getEndTime().isAfter(now)) {
			throw new RuntimeException("無法為尚未完成的預約提供評分");
		}

		reservation.setRating(rating);
		reservation.setRatingContent(ratingContent);
		reservationRepository.save(reservation);
	}

	@Transactional
	public void submitFeedback(Integer rsvNo, Integer ftId, String feedback, String note) {
		ReservationVO reservation = reservationRepository.findById(rsvNo)
				.orElseThrow(() -> new RuntimeException("找不到預約"));

		// Verify that this fortune teller is associated with this reservation
		if (!reservation.getFtId().getFtId().equals(ftId)) {
			throw new RuntimeException("您無權為此預約提供回饋");
		}

//		if (reservation.getRsvStatus() != 1 || reservation.getPayment() != 1) {
//			throw new RuntimeException("無法為未付款或未完成的預約提供回饋");
//		}

		LocalDateTime now = LocalDateTime.now();
		// Verify reservation status, payment, and completion
		if (reservation.getRsvStatus() != 1 || reservation.getPayment() != 1
				|| reservation.getAvailableTimeNo().getEndTime().isAfter(now)) {
			throw new RuntimeException("無法為未付款、未完成或尚未結束的預約提供回饋");
		}

		reservation.setFtFeedback(feedback);
		reservation.setNote(note);
		reservationRepository.save(reservation);
	}

	public Map<String, Object> getReservationStatistics() {
		List<ReservationVO> reservations = reservationRepository.findAll();
		Map<String, Object> statistics = new HashMap<>();

		statistics.put("totalReservations", reservations.size());
		statistics.put("completedReservations", reservations.stream().filter(r -> r.getRsvStatus() == 1).count());
		statistics.put("cancelledReservations", reservations.stream().filter(r -> r.getRsvStatus() == 2).count());
		statistics.put("pendingReservations", reservations.stream().filter(r -> r.getRsvStatus() == 0).count());

		Map<Integer, Long> reservationsPerFt = reservations.stream()
				.collect(Collectors.groupingBy(r -> r.getFtId().getFtId(), Collectors.counting()));
		statistics.put("reservationsPerFt", reservationsPerFt);

		Double totalSpent = reservations.stream().mapToDouble(ReservationVO::getPrice).sum();
		statistics.put("totalSpent", totalSpent);

		Map<String, Long> reservationsPerSkill = reservations.stream()
				.collect(Collectors.groupingBy(r -> r.getSkillNo().getSkillName(), Collectors.counting()));
		statistics.put("reservationsPerSkill", reservationsPerSkill);

		List<String> skillLabels = new ArrayList<>(reservationsPerSkill.keySet());
		List<Long> skillCounts = new ArrayList<>(reservationsPerSkill.values());
		statistics.put("skillLabels", skillLabels);
		statistics.put("skillCounts", skillCounts);

		return statistics;
	}

	public ReservationVO getMemberReservationByRsvNo(int rsvNo, Integer memberId) {
		Optional<ReservationVO> reservationOpt = reservationRepository.findByRsvNo(rsvNo);
		if (reservationOpt.isPresent()) {
			ReservationVO reservation = reservationOpt.get();
			if (reservation.getMemberId().getMemberId().equals(memberId)) {
				return reservation;
			}
		}
		return null;
	}

	public ReservationVO getFtReservationByRsvNo(int rsvNo, Integer ftId) {
		Optional<ReservationVO> reservationOpt = reservationRepository.findByRsvNo(rsvNo);
		if (reservationOpt.isPresent()) {
			ReservationVO reservation = reservationOpt.get();
			if (reservation.getFtId().getFtId().equals(ftId)) {
				return reservation;
			}
		}
		return null;
	}
}
