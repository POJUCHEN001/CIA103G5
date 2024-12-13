package com.cia103g5.user.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//	public void updateReservation(int rsvNo, byte newStatus) {
//		ReservationVO reservation = getReservationByRsvNo(rsvNo);
//		if (reservation != null) {
//			reservation.setRsvStatus(newStatus);
//			reservationRepository.save(reservation);
//		} else {
//			throw new RuntimeException("Reservation not found");
//		}
//	}

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

//    public Map<String, Object> getReservationStatistics() {
//        List<ReservationVO> reservations = reservationRepository.findAll();
//        Map<String, Object> statistics = new HashMap<>();
//        int totalReservations = reservations.size();
//        statistics.put("totalReservations", totalReservations);
//
//        Map<Integer, Long> reservationsPerFt = reservations.stream()
//                .collect(Collectors.groupingBy(r -> r.getFtId().getFtId(), Collectors.counting()));
////        .collect(Collectors.groupingBy(r -> r.getFtVO().getFtId(), Collectors.counting()));
//        statistics.put("reservationsPerFt", reservationsPerFt);
//
//        return statistics;
//    }

	public Map<String, Object> getMemberReservationStatistics(Integer memberId) {
		List<ReservationVO> reservations = getReservationsForMember(memberId);
		Map<String, Object> statistics = new HashMap<>();

		statistics.put("totalReservations", reservations.size());
		statistics.put("completedReservations", reservations.stream().filter(r -> r.getRsvStatus() == 1).count());
		statistics.put("cancelledReservations", reservations.stream().filter(r -> r.getRsvStatus() == 2).count());
		statistics.put("pendingReservations", reservations.stream().filter(r -> r.getRsvStatus() == 0).count());
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

		statistics.put("totalReservations", reservations.size());
		statistics.put("completedReservations", reservations.stream().filter(r -> r.getRsvStatus() == 1).count());
		statistics.put("cancelledReservations", reservations.stream().filter(r -> r.getRsvStatus() == 2).count());
		statistics.put("pendingReservations", reservations.stream().filter(r -> r.getRsvStatus() == 0).count());
		statistics.put("totalEarnings", reservations.stream().mapToInt(ReservationVO::getPrice).sum());

		// Add more statistics as needed

		return statistics;
	}

	public Map<String, Object> getMemberFinancialData(Integer memberId) {
		List<ReservationVO> reservations = getReservationsForMember(memberId);
		Map<String, Object> financialData = new HashMap<>();

		// Get bank account (assuming it's stored in the member information)
		MemberVO member = getMemberVO(memberId);
		financialData.put("bankAccount", member.getBankAccount());

		// Calculate monthly spending
		LocalDateTime now = LocalDateTime.now();
		int monthlySpending = reservations.stream().filter(
				r -> r.getPaymentTime().getMonth() == now.getMonth() && r.getPaymentTime().getYear() == now.getYear())
				.mapToInt(ReservationVO::getPrice).sum();
		financialData.put("monthlySpending", monthlySpending);

		// Calculate yearly spending
		int yearlySpending = reservations.stream().filter(r -> r.getPaymentTime().getYear() == now.getYear())
				.mapToInt(ReservationVO::getPrice).sum();
		financialData.put("yearlySpending", yearlySpending);

		// Get transaction details
		List<Map<String, Object>> transactions = reservations.stream().map(r -> {
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
		// Implement logic to fetch and calculate financial data for fortune tellers
		// This should include balance, transactions, monthly/yearly earnings, etc.

		int totalEarnings = reservations.stream().mapToInt(ReservationVO::getPrice).sum();
		financialData.put("totalEarnings", totalEarnings);

		return new HashMap<>(); // Placeholder
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
