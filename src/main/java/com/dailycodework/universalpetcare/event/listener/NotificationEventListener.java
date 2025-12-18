package com.dailycodework.universalpetcare.event.listener;

import com.dailycodework.universalpetcare.email.EmailService;
import com.dailycodework.universalpetcare.event.*;
import com.dailycodework.universalpetcare.model.Appointment;
import com.dailycodework.universalpetcare.model.User;
import com.dailycodework.universalpetcare.service.token.IVerificationTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationEventListener implements ApplicationListener<ApplicationEvent> {
    private final EmailService emailService;
    private final IVerificationTokenService tokenService;

    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Object source = event.getSource();
        switch (event.getClass().getSimpleName()) {

            case "RegistrationCompleteEvent":
                if (source instanceof User) {
                    handleSendRegistrationVerificationEmail((RegistrationCompleteEvent) event);
                }
                break;

            case "AppointmentBookedEvent":
                if (source instanceof Appointment) {
                    try {
                        handleAppointmentBookedNotification((AppointmentBookedEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "AppointmentApprovedEvent":
                if (source instanceof Appointment) {
                    try {
                        handleAppointmentApprovedNotification((AppointmentApprovedEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "AppointmentDeclinedEvent":
                if (source instanceof Appointment) {
                    try {
                        handleAppointmentDeclinedNotification((AppointmentDeclinedEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "PasswordResetEvent":
                PasswordResetEvent passwordResetEvent = (PasswordResetEvent) event;
                handlePasswordResetRequest(passwordResetEvent);
                break;


            default:
                break;
        }
    }


    /*=================== Start user registration email verification ============================*/
    private void handleSendRegistrationVerificationEmail(RegistrationCompleteEvent event) {
        User user = event.getUser();
        // Generate a token for the user
        String vToken = UUID.randomUUID().toString();
        // Save the token for the user
        tokenService.saveVerificationTokenForUser(vToken, user);
        // Build the verification url
        String verificationUrl = frontendBaseUrl + "/email-verification?token=" + vToken;
        try {
            sendRegistrationVerificationEmail(user, verificationUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRegistrationVerificationEmail(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Xác thực email";
        String senderName = "Pet Care";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" +
                "<p>Cảm ơn bạn đã đăng ký tài khoản tại hệ thống của chúng tôi," +
                "Vui lòng nhấp vào liên kết dưới đây để hoàn tất quá trình đăng ký.</p>" +
                "<a href=\"" + url + "\">Xác thực email</a>" +
                "<p> Xin chân thành cảm ơn <br> Dịch vụ Xác minh Email của Pet Care";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*=================== End user registration email verification ============================*/



    /*======================== Start New Appointment booked notifications ===================================================*/

    private void handleAppointmentBookedNotification(AppointmentBookedEvent event) throws MessagingException, UnsupportedEncodingException {
        Appointment appointment = event.getAppointment();
        User vet = appointment.getVeterinarian();
        sendAppointmentBookedNotification(vet, frontendBaseUrl);
    }

    private void sendAppointmentBookedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Thông báo lịch hẹn mới";
        String senderName = "Pet Care";
        String mailContent = "<p>Chào bạn, " + user.getFirstName() + ", </p>" +
                "<p>Bạn có một lịch hẹn mới được lên lịch:</p>" +
                "<a href=\"" + url + "\">Vui lòng kiểm tra cổng thông tin của phòng khám để xem chi tiết lịch hẹn.</a> <br/>" +
                "<p> Trân trọng.<br> Dịch vụ Chăm sóc Thú cưng Pet Care";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End New Appointment Booked notifications ===================================================*/


    /*======================== Start Approve Appointment notifications ===================================================*/

    private void handleAppointmentApprovedNotification(AppointmentApprovedEvent event) throws MessagingException, UnsupportedEncodingException {
        Appointment appointment = event.getAppointment();
        User patient = appointment.getPatient();
        sendAppointmentApprovedNotification(patient, frontendBaseUrl);
    }

    private void sendAppointmentApprovedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Lịch hẹn đã được duyệt";
        String senderName = "Pet Care";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" +
                "<p>Lịch hẹn của bạn đã được chấp thuận:</p>" +
                "<a href=\"" + url + "\">Vui lòng kiểm tra cổng thông tin của phòng khám để xem chi tiết lịch hẹn " +
                "và thông tin bác sĩ thú y.</a> <br/>" +
                "<p> Trân trọng.<br> Pet Care";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End Approve Appointment notifications ===================================================*/


    /*======================== Start Decline Appointment notifications ===================================================*/

    private void handleAppointmentDeclinedNotification(AppointmentDeclinedEvent event) throws MessagingException, UnsupportedEncodingException {
        Appointment appointment = event.getAppointment();
        User patient = appointment.getPatient();
        sendAppointmentDeclinedNotification(patient, frontendBaseUrl);
    }

    private void sendAppointmentDeclinedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Lịch hẹn đã bị từ chối";
        String senderName = "Pet care";
        String mailContent = "<p> Chào bạn, " + user.getFirstName() + ", </p>" +
                "<p>Chúng tôi xin lỗi, lịch hẹn của bạn hiện chưa được chấp thuận.<br/> " +
                "Vui lòng đặt lại lịch hẹn vào một ngày khác. Xin cảm ơn.</p>" +
                "<a href=\"" + url + "\">Vui lòng kiểm tra cổng thông tin của phòng khám để xem chi tiết lịch hẹn.</a> <br/>" +
                "<p> Trân trọng.<br> Pet Care";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End Decline Appointment notifications ===================================================*/



    /*======================== Start password reset related notifications ===================================================*/

    private void handlePasswordResetRequest(PasswordResetEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.saveVerificationTokenForUser(token, user);
        String resetUrl = frontendBaseUrl + "/reset-password?token=" + token;
        try {
            sendPasswordResetEmail(user, resetUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu", e);
        }
    }

    private void sendPasswordResetEmail(User user, String resetUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Yêu cầu đặt lại mật khẩu";
        String senderName = "Universal Pet Care";
        String mailContent = "<p>Chào bạn, " + user.getFirstName() + ",</p>" +
                "<p>Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng nhấp vào liên kết dưới đây để tiếp tục:</p>" +
                "<a href=\"" + resetUrl + "\">Đặt lại Mật khẩu</a><br/>" +
                "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>" +
                "<p>Trân trọng.<br> Pet Care</p>";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End password reset related notifications ===================================================*/
}
