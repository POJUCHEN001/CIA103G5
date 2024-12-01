package com.cia103g5.common.noitfy;

import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.memberNotify.model.MemberNotifyService;
import com.cia103g5.user.memberNotify.model.MemberNotifyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**#############################
 #                             #
 #          通知系統            #
 #                             #
 ##############################*/

@Component
public class Notify {

    private static MemberNotifyService memberNotifyService;
//    private static MailService mailService;

    /**
     * 注入所需的服務，讓靜態方法可以使用
     */
    @Autowired
    public Notify(MemberNotifyService memberNotifyService) {
        Notify.memberNotifyService = memberNotifyService;
//        Notify.mailService = mailService;
    }

    /**
     * 發送通知給指定會員（不發送郵件）
     *
     * @param notifyType 通知類型
     * @param member     接收通知的會員
     * @param title      通知標題
     * @param msg        通知內容
     */
    public static void notifyByMember(NotifyType notifyType, MemberVO member, String title, String msg) {
        notifyByMember(notifyType, member, title, msg, false);
    }

    /**
     * 發送通知給指定會員（可選擇是否發送郵件）
     *
     * @param notifyType 通知類型
     * @param member     接收通知的會員
     * @param title      通知標題
     * @param msg        通知內容
     * @param mail       是否發送郵件
     */
    public static void notifyByMember(NotifyType notifyType, MemberVO member, String title, String msg, Boolean mail) {
        // 創建通知實例
        MemberNotifyVo notify = new MemberNotifyVo();
        notify.setNotifyType(notifyType.name());
        notify.setMember(member);
        notify.setTitle(title);
        notify.setContent(msg);
        notify.setNotifiedTime(new Date());
        notify.setStatus(0); // 未讀
        notify.setSendMail(mail != null && mail);

        // 保存到數據庫
        memberNotifyService.save(notify);

        // 如果需要發送郵件，調用郵件服務
        if (Boolean.TRUE.equals(mail)) {
            String email = member.getEmail();
            if (email != null && !email.isEmpty()) {
//                mailService.sendEmail(email, title, msg);
            }
        }
    }
}
