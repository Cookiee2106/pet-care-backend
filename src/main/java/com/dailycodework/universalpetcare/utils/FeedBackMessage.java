package com.dailycodework.universalpetcare.utils;

public class FeedBackMessage {

    /*
     * ======================== Start User API=====================================
     */
    public static final String CREATE_USER_SUCCESS = "Tạo tài khoản người dùng thành công";
    public static final String DELETE_USER_SUCCESS = "Xóa tài khoản người dùng thành công";
    public static final String USER_UPDATE_SUCCESS = "Cập nhật thông tin người dùng thành công";
    public static final String USER_FOUND = "Tìm thấy người dùng";
    public static final String USER_NOT_FOUND = "Xin lỗi, không tìm thấy người dùng";
    public static final String NO_USER_FOUND = "Ôi không, không tìm thấy người dùng với : ";
    public static final String LOCKED_ACCOUNT_SUCCESS = "Khóa tài khoản thành công";
    public static final String UNLOCKED_ACCOUNT_SUCCESS = "Mở khóa tài khoản thành công";
    /* ======================== End User API===================================== */

    /*
     * ======================== Start Password
     * API=====================================
     */
    public static final String PASSWORD_CHANGE_SUCCESS = "Đổi mật khẩu thành công! Bây giờ bạn có thể đóng biểu mẫu này.";
    public static final String PASSWORD_RESET_EMAIL_SENT = "Đã gửi liên kết đến email của bạn, vui lòng kiểm tra email để hoàn tất yêu cầu đặt lại mật khẩu";
    public static final String MISSING_PASSWORD = "Thiếu mã xác thực hoặc mật khẩu";
    public static final String INVALID_RESET_TOKEN = "Mã xác thực đặt lại mật khẩu không hợp lệ";
    public static final String INVALID_EMAIL = "Vui lòng nhập địa chỉ email liên kết với tài khoản của bạn.";
    public static final String PASSWORD_RESET_SUCCESS = "Mật khẩu của bạn đã được đặt lại thành công!";

    /*
     * ======================== End Password
     * API=====================================
     */

    /*
     * ======================== Start Appointment
     * API=====================================
     */
    public static final String APPOINTMENT_UPDATE_SUCCESS = "Cập nhật lịch hẹn thành công";
    public static final String APPOINTMENT_APPROVED_SUCCESS = "Lịch hẹn đã được chấp thuận thành công";
    public static final String APPOINTMENT_DECLINED_SUCCESS = "Lịch hẹn đã bị từ chối thành công";
    public static final String APPOINTMENT_CANCELLED_SUCCESS = "Hủy lịch hẹn thành công";
    public static final String APPOINTMENT_DELETE_SUCCESS = "Xóa lịch hẹn thành công";
    public static final String APPOINTMENT_BOOKED_SUCCESS = "Đặt lịch hẹn thành công";
    public static final String APPOINTMENT_FOUND = "Tìm thấy lịch hẹn";
    public static final String APPOINTMENT_NOT_FOUND = "Không tìm thấy lịch hẹn";
    public static final String APPOINTMENT_UPDATE_NOT_ALLOWED = "Không thể cập nhật hoặc hủy lịch hẹn";
    public static final String OPERATIONS_NOT_ALLOWED = "Thao tác không được cho phép";
    public static final String INVALID_APPOINTMENT_DATE = "Không thể đặt lịch hẹn trong quá khứ.";
    public static final String INVALID_APPOINTMENT_TIME = "Không thể đặt lịch hẹn cho thời gian đã qua.";
    public static final String INVALID_APPOINTMENT_HOURS = "Lịch hẹn chỉ được phép trong khoảng thời gian từ 08:00 sáng đến 18:00 tối.";
    /*
     * ======================== End Appointment
     * API=====================================
     */

    /*
     * ======================== Start Pet API=====================================
     */
    public static final String PET_ADDED_SUCCESS = "Thêm thú cưng vào lịch hẹn thành công";
    public static final String PET_UPDATE_SUCCESS = "Cập nhật thông tin thú cưng thành công";
    public static final String PET_DELETE_SUCCESS = "Xóa hồ sơ thú cưng thành công";
    public static final String PET_FOUND = "Tìm thấy thú cưng";
    public static final String PET_NOT_FOUND = "Không tìm thấy thú cưng";

    /* ======================== End Pet API===================================== */

    /*
     * ======================== Start Review
     * API=====================================
     */
    public static final String REVIEW_NOT_ALLOWED = "Xin lỗi, chỉ những bệnh nhân đã hoàn thành lịch khám với bác sĩ này mới có thể để lại đánh giá";
    public static final String ALREADY_REVIEWED = "Bạn đã đánh giá bác sĩ này. Bạn có thể chỉnh sửa nhận xét trước đó";
    public static final String CANNOT_REVIEW = "Bác sĩ thú y không thể tự đánh giá chính mình";
    public static final String VET_OR_PATIENT_NOT_FOUND = "Không tìm thấy Bác sĩ thú y hoặc Bệnh nhân";
    public static final String NO_VETS_AVAILABLE = "Không có bác sĩ thú y nào khả dụng vào ngày giờ yêu cầu";
    public static final String REVIEW_SUBMIT_SUCCESS = "Gửi đánh giá thành công";
    public static final String REVIEW_UPDATE_SUCCESS = "Cập nhật đánh giá thành công";
    public static final String REVIEW_DELETE_SUCCESS = "Xóa đánh giá thành công";
    public static final String REVIEW_FOUND = "Tìm thấy đánh giá";

    /*
     * ======================== End Review API=====================================
     */
    public static final String NOT_ALLOWED = "Bạn phải có một lịch khám hoàn thành với bác sĩ này để để lại đánh giá";

    /*
     * ======================== Start General feedback
     * =====================================
     */
    public static final String SUCCESS = "Thành công!";
    public static final String RESOURCE_FOUND = "Tìm thấy tài nguyên";
    public static final String SENDER_RECIPIENT_NOT_FOUND = "Không tìm thấy người gửi hoặc người nhận";
    public static final String ERROR = "Đã xảy ra lỗi";
    public static final String RESOURCE_NOT_FOUND = "Không tìm thấy tài nguyên";;
    /*
     * ======================== End general feedback
     * =====================================
     */

    /*
     * ======================== Start authentication feedback
     * =====================================
     */
    public static final String EMPTY_PASSWORD = "Tất cả các trường là bắt buộc";
    public static final String INCORRECT_PASSWORD = "Mật khẩu không chính xác";
    public static final String PASSWORD_MISMATCH = "Xác nhận mật khẩu không khớp";
    public static final String AUTHENTICATION_SUCCESS = "Xác thực thành công";
    public static final String ACCOUNT_DISABLED = "Xin lỗi, tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ bộ phận hỗ trợ";
    public static final String INVALID_PASSWORD = "Tên người dùng hoặc mật khẩu không hợp lệ";
    public static final String UNAUTHORIZED = "Vui lòng đăng nhập để gửi đánh giá.";

    /*
     * ======================== End authentication feedback
     * =====================================
     */

    /*
     * ======================== Start Token API
     * =====================================
     */
    public static final String INVALID_TOKEN = "KHÔNG HỢP LỆ";
    public static final String TOKEN_ALREADY_VERIFIED = "ĐÃ ĐƯỢC XÁC MINH";
    public static final String EXPIRED_TOKEN = "ĐÃ HẾT HẠN";
    public static final String VALID_VERIFICATION_TOKEN = "HỢP LỆ";
    public static final String TOKEN_VALIDATION_ERROR = "Lỗi xác thực mã";
    public static final String TOKEN_SAVED_SUCCESS = "Lưu mã xác minh thành công";
    public static final String TOKEN_DELETE_SUCCESS = "Xóa mã người dùng thành công";
    public static final String INVALID_VERIFICATION_TOKEN = "Mã xác minh không hợp lệ";
    public static final String NEW_VERIFICATION_TOKEN_SENT = "Đã gửi một liên kết xác minh mới đến email của bạn. Vui lòng kiểm tra để hoàn tất đăng ký.";

    /*
     * ======================== End Token API =====================================
     */

    /*
     * ======================== Start Photo API
     * =====================================
     */
    public static final String PHOTO_UPDATE_SUCCESS = "Cập nhật ảnh thành công";;
    public static final String PHOTO_REMOVE_SUCCESS = "Xóa ảnh thành công";
    /*
     * ======================== End Photo API =====================================
     */

    /*
     * ======================== Start Role API =====================================
     */
    public static final String ROLE_NOT_FOUND = "Không tìm thấy vai trò";

    /*
     * ======================== End Role API =====================================
     */

}
