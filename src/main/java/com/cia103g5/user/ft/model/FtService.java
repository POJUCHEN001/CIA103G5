package com.cia103g5.user.ft.model;

import com.cia103g5.user.ftgrade.model.FtGrade;
import com.cia103g5.user.ftgrade.model.FtGradeRepository;
import com.cia103g5.user.member.model.MemberRepository;
import com.cia103g5.user.member.model.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
public class FtService {

    @Autowired
    private FtRepository ftRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FtGradeRepository ftGradeRepository;


    // 新增占卜師
    public void addFortuneTeller(Integer memberId, String companyName, MultipartFile photo, MultipartFile businessPhoto, String businessNo, String nickname)  {

        // 驗證 memberId 並獲取對應的 MemberVO
        MemberVO member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("無效的會員編號: " + memberId));

        FtGrade ftGrade = ftGradeRepository.findByFtRank(1)
                .orElseThrow(() -> new IllegalArgumentException("無效的 FtRank 預設值"));

        Date registeredTime = new Date();

        FtVO fortuneTeller = new FtVO();
        fortuneTeller.setMember(member);
        fortuneTeller.setFtRank(ftGrade);
        fortuneTeller.setCompanyName(companyName);
        fortuneTeller.setBusinessNo(businessNo);
        fortuneTeller.setNickname(nickname);
        fortuneTeller.setRegisteredTime(registeredTime);
        // 尚未審核 占卜師服務預設關閉(0)
        fortuneTeller.setCanPost((byte) 0);
        fortuneTeller.setCanSell((byte) 0);
        fortuneTeller.setCanRev((byte) 0);


        processPhotos(fortuneTeller, new MultipartFile[]{photo, businessPhoto});
        validateRegisteredInfo(fortuneTeller);

        ftRepository.save(fortuneTeller);

    }

    // 查詢占卜師編號(依據會員編號)
    public Integer findFtIdByMemberId(Integer memberId){
        // 回傳占卜師編號，若沒有占卜師編號回傳0(即沒有占卜師身分)
        return ftRepository.findFtIdByMemId(memberId).orElse(0);
    }

    // 查詢占卜師資料
    public FtVO findFtByFtId(Integer ftId){
        return ftRepository.findById(ftId)
                .orElseThrow(() -> new RuntimeException("占卜師不存在"));
    }

    // 更新占卜師形象照
    public void updateFortuneTellerProfilePhoto(Integer ftId, MultipartFile photo){
        FtVO fortuneTeller = findFtOrThrow(ftId);
        processPhoto(fortuneTeller, photo);
        ftRepository.save(fortuneTeller);
    }

    // 占卜師設置個人簡介與服務價格
    public void updateFortuneTellerIntroduction(Integer ftId, String intro, String nickname, Integer price) {
        FtVO fortuneTeller = findFtOrThrow(ftId); // 先查詢原始資料
        fortuneTeller.setNickname(nickname);
        fortuneTeller.setIntro(intro);
        fortuneTeller.setPrice(price);

        ftRepository.save(fortuneTeller);
    }

    // 占卜師服務狀態變更
    public FtVO updateFortuneTellerServiceStatus(FtVO ftId, Byte canRev, Byte canSell, Byte canPost) {
        FtVO fortuneTeller = findFtOrThrow(ftId.getFtId());
        // 0不可 1 可
        fortuneTeller.setCanRev(canRev);
        fortuneTeller.setCanSell(canSell);
        fortuneTeller.setCanPost(canPost);

        return ftRepository.save(fortuneTeller);
    }


    // 啟用占卜師
    public void updateFortuneTellerState(Integer ftId) {
        Date approvedDate = new Date();
        FtVO fortuneTeller = findFtOrThrow(ftId);
        fortuneTeller.setStatus((byte) 1);
        fortuneTeller.setApprovedTime(approvedDate);
        ftRepository.save(fortuneTeller);
    }

    // 占卜師權限關閉
    public void updateFortuneTellerState (Integer ftId, byte state){
        FtVO fortuneTeller = findFtOrThrow(ftId);
        fortuneTeller.setStatus(state);
        ftRepository.save(fortuneTeller);
    }

    // 查詢所有占卜師
    public List<FtVO> getFtListWith() {
        return ftRepository.findAll();
    }


    // 通用方法：處理單張照片
    private void processPhoto(FtVO fortuneTeller, MultipartFile photo) {
        try {
            if (photo != null && !photo.isEmpty()) {
                fortuneTeller.setPhoto(photo.getBytes());
            } else {
                throw new IllegalArgumentException("請上傳有效的照片");
            }
        } catch (IOException e) {
            throw new RuntimeException("照片處理失敗：" + e.getMessage(), e);
        }
    }


    // 通用方法：處理多張照片
    private void processPhotos(FtVO fortuneTeller, MultipartFile[] photos) {
        String[] photoFields = {"photo", "businessPhoto"};
        try{
            for(int i = 0; i < photos.length; i++){
                if (photos[i] != null)
                    setPhotoField(fortuneTeller, photos[i], photoFields[i]);
            }
        } catch(IOException ie){
            System.err.println("照片處理失敗" + ie.getMessage());
        }

    }

    private void setPhotoField(FtVO fortuneTeller, MultipartFile photo, String fieldName) throws IOException {
        byte[] photoBytes = photo.getBytes();
        switch (fieldName) {
            case "photo":
                fortuneTeller.setPhoto(photoBytes);
                break;
            case "businessPhoto":
                fortuneTeller.setBusinessPhoto(photoBytes);
                break; // 可以根據需要添加更多欄位處理
            default:
                throw new IllegalArgumentException("Unknown photo field: " + fieldName);
        }
    }

    // 通用方法: 註冊資料驗證
    private void validateRegisteredInfo(FtVO fortuneTeller) {
        if(fortuneTeller.getCompanyName() == null || fortuneTeller.getCompanyName().trim().isEmpty()) {
            throw new IllegalArgumentException("公司名不得為空");
        }
        if(fortuneTeller.getBusinessNo() == null || fortuneTeller.getBusinessNo().trim().isEmpty()) {
            throw new IllegalArgumentException("營業登記編號不得為空");
        }
        if(fortuneTeller.getNickname() == null || fortuneTeller.getNickname().trim().isEmpty()) {
            throw new IllegalArgumentException("暱稱不得為空");
        }

    }

    // 查詢占卜師
    private FtVO findFtOrThrow(Integer ftId) {
        return ftRepository.findById(ftId)
                .orElseThrow(() -> new IllegalArgumentException("占卜師不存在"));
    }

}
