package family_tree.service.implementation;

import family_tree.logger.Logger;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final Logger securityLogger;

    @Value("${spring.mail.username}")
    private String from;

    public void sendVerificationCodeEmail(String email, String code) {
        String subject = "Your verification code";
        String path = "/verify"; // page where user can enter code
        String message = "Use the code below to verify your email:";
        String templateName = "email/verification_code.html";

        try {
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .toUriString();
            Context contextForThymeleaf = new Context(); // for thymeleaf
            contextForThymeleaf.setVariables(Map.of(
                    "subject", subject,
                    "message", message,
                    "code", code,
                    "actionUrl", actionUrl
            ));

            String context = templateEngine.process(templateName, contextForThymeleaf);
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(context, true); // true = HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            // Log the failure but do NOT rethrow - registration should not fail because of email issues
            securityLogger.logEmailSendError(email);
            securityLogger.logGeneralError(e instanceof Exception ? (Exception) e : new Exception(e));
            // swallow the exception so user registration flow continues; admins can inspect logs
        }
    }

}
