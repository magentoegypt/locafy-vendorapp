//
//  AuthenticationView.swift
//  MageNative Magento Platinum
//
//  Created by Hamza Usmani on 25/01/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

final class AuthenticationView: UIView {
    
    // MARK:- PROPERTIES
    
    var otpCode: String = ""
    
    // MARK:- VIEWS
    
    lazy private var containerView: UIView = {
        let view = UIView()
        view.backgroundColor    = DynamicColor.ViewBackgroundColor
        view.layer.cornerRadius = 8.0
        view.clipsToBounds = true
        return view
    }()
    lazy private var headingLabel: UILabel = {
        let label  = UILabel()
        label.text = "Please Enter the OTP send to your mobile number".localized
        label.font = .systemFont(ofSize: 12, weight: .semibold)
        label.textAlignment = .center
        return label
    }()
    
    lazy private var otpField: SkyFloatingLabelTextField = {
        let field = SkyFloatingLabelTextField()
        field.placeholder = "Please Enter OTP".localized
        return field
    }()
    
    lazy private var verifyOtpButton: UIButton = {
        let button = UIButton()
        button.setTitle("Verify Otp".localized, for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.backgroundColor  = UIColor.green
        button.titleLabel?.font = .systemFont(ofSize: 13, weight: .medium)
        button.addTarget(self, action: #selector(validateOTP), for: .touchUpInside)
        return button
    }()
    
    lazy private var resendOtpButton: UIButton = {
        let button = UIButton()
        button.setTitle("Regenerate Otp".localized, for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.backgroundColor  = DynamicColor.themeColor
        button.titleLabel?.font = .systemFont(ofSize: 13, weight: .medium)
        button.addTarget(self, action: #selector(resendOtp), for: .touchUpInside)
        return button
    }()
    
    // MARK:- Callbacks
    
    var onSuccess: ((String) -> Void)?
    var onFailure: ((String) -> Void)?
    var onResend: (() -> Void)?
    
    
    // MARK:- LIFE CYCLE
    
    
    // MARK:- INITS
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        configureUI()
        
        let tapGesture:UITapGestureRecognizer = .init(target: self, action: #selector(remove))
        tapGesture.delegate = self
        addGestureRecognizer(tapGesture)
    }
    
    convenience init(otp: String) {
        self.init()
        otpCode = otp
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK:- SELECTORS
    
    @objc private func remove() {
        removeFromSuperview()
    }
    
    @objc private func validateOTP() {
        let currentOTP: String = otpField.text ?? ""
        if currentOTP.isEmpty { makeToast("Please Enter OTP".localized); return }
        
      //  onSuccess?(currentOTP)
        
        if currentOTP == otpCode {
            onSuccess?(currentOTP)
        } else {
            onFailure?("OTP does not match".localized)
            return
        }
        remove()
    }
    
    @objc private func resendOtp() {
        onResend?()
    }
    
    
    
    // MARK:- HELPER FUNCTIONS
    
    private func configureUI() {
        backgroundColor = UIColor.black.withAlphaComponent(0.5)
        
        // TODO:- Add Subviews
        addSubview(containerView)
        containerView.anchor(left: leadingAnchor, right: trailingAnchor, paddingLeft: 32, paddingRight: 32, height: 160)
        containerView.centerY(inView: self)
        
        containerView.addSubview(headingLabel)
        headingLabel.anchor(top: containerView.topAnchor, left: containerView.leadingAnchor, right: containerView.trailingAnchor, paddingTop: 8, paddingLeft: 16, paddingRight: 16)
        
        containerView.addSubview(otpField)
        otpField.anchor(top: headingLabel.bottomAnchor, left: containerView.leadingAnchor, right: containerView.trailingAnchor, paddingTop: 8, paddingLeft: 16, paddingRight: 16)
        
        let stack          = UIStackView(arrangedSubviews: [verifyOtpButton, resendOtpButton])
        stack.axis         = .horizontal
        stack.distribution = .fillEqually
        stack.spacing = 0.5
        
        containerView.addSubview(stack)
        stack.anchor(left: containerView.leadingAnchor, bottom: containerView.bottomAnchor, right: containerView.trailingAnchor, height: 35)
    }
    
}

extension AuthenticationView: UIGestureRecognizerDelegate {
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        guard let view = touch.view else { return true }
        return !view.isDescendant(of: containerView)
    }
}
