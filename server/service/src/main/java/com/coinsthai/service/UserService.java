package com.coinsthai.service;

import com.coinsthai.model.IdCard;
import com.coinsthai.model.User;
import com.coinsthai.vo.user.CellphoneUpdateRequest;
import com.coinsthai.vo.user.PasswordResetEmailRequest;
import com.coinsthai.vo.user.PasswordResetRequest;
import com.coinsthai.vo.user.PasswordUpdateRequest;

/**
 * @author
 */
public interface UserService {

    User create(User user);

    User update(User user);

    User updateCellphone(CellphoneUpdateRequest request);

    User updatePassword(PasswordUpdateRequest request);

    User get(String id);

    User getByLogin(String emailOrCellphone);

    IdCard getByUser(String userId);

    // ====== 找回密码 =====

    void requestPasswordReset(PasswordResetEmailRequest request);

    User resetPassword(PasswordResetRequest request);

    void sendPasswordResetEmail(String email);
}
