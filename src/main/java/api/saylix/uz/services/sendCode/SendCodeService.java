package api.saylix.uz.services.sendCode;

import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.enums.SendCode.CodeType;
import api.saylix.uz.enums.SendCode.TypeOfSendCodeProvider;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.services.AppLanguageService;
import api.saylix.uz.utils.EmailTemplateUtil;
import api.saylix.uz.utils.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Service
public class SendCodeService {

    @Value("${spring.mail.username}")
    private String fromEmailAccount;

    @Value("${server.domain}")
    private String serverDomain;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AppLanguageService getLanguage;

    @Autowired
    private SendCodeHistoryService sendCodeHistoryService;

    private final int smsLimit = 3;

    public void sendCodeForRegisterUser(String username, AppLanguage language) {
        String code = RandomUtil.getRandomSmsCode();
        String subject = getEmailSubject(language);
        String body = EmailTemplateUtil.buildRegistrationEmailBody(code, language);

        // save code history
        saveSendCodeHistory(username, subject, code, TypeOfSendCodeProvider.EMAIL, CodeType.REGISTRATION, language);
        sendCodeToEmail(username, subject, body);
    }

    public void sendCodeForUsernameUpdate(String username, AppLanguage language) {
        String code = RandomUtil.getRandomSmsCode();
        String subject = getEmailSubject(language);
        String body = EmailTemplateUtil.buildUpdateUsernameBody(code, language);

        // save code history
        saveSendCodeHistory(username, subject, code, TypeOfSendCodeProvider.EMAIL, CodeType.UPDATE_USERNAME, language);
        sendCodeToEmail(username, subject, body);
    }

    public void sendCodeForResetPassword(String username, AppLanguage language) {
        String code = RandomUtil.getRandomSmsCode();
        String subject = getEmailSubject(language);
        String body = EmailTemplateUtil.buildPasswordResetEmailBody(code, language);

        // save code history
        saveSendCodeHistory(username, subject, code, TypeOfSendCodeProvider.EMAIL, CodeType.RESEND_PASSWORD, language);
        sendCodeToEmail(username, subject, body);
    }

    private void saveSendCodeHistory(String username, String code, String message,
                                     TypeOfSendCodeProvider type, CodeType codeType, AppLanguage language) {
        Long count = sendCodeHistoryService.getSmsCount(username);
        if (count >= smsLimit) {
            throw new AppBadException(getLanguage.getMessage("send.code.many.times", language));
        }
        sendCodeHistoryService.create(username, code, message, type, codeType);
    }

    private String getEmailSubject(AppLanguage language) {
        return switch (language) {
            case uz -> "Tasdiqlash uchun";
            case ru -> "Подтвердить аккаунт";
            case en -> "Confirm account";
            case jp -> "Confirm account jp";
        };
    }

    private void sendCodeToEmail(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmailAccount);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }
}
