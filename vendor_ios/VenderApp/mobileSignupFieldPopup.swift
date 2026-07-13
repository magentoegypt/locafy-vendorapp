//
//  mobileSignupFieldPopup.swift
//  VenderApp
//
//  Created by Cedcoss on 31/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class mobileSignupFieldPopup: UIView {

    @IBOutlet weak var topHeading: UILabel!
    @IBOutlet weak var mobileField: UITextField!
    @IBOutlet weak var verifyphoneNumberView: JNPhoneNumberView!
    @IBOutlet weak var continueBtn: UIButton!
    
    var vendorRegistration:ced_vendorRegistration?
    var view:UIView!
    
    override init(frame: CGRect) {
        // 1. setup any properties here
        
        // 2. call super.init(frame:)
        super.init(frame: frame)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        // 1. setup any properties here
        
        // 2. call super.init(coder:)
        super.init(coder: aDecoder)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    @objc func xibSetup() {
        view = loadViewFromNib()
       // self.view.tag=1998
        // use bounds not frame or it'll be offset
        view.frame = bounds
        
        // Make the view stretch with containing view
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
        
        topHeading.backgroundColor = DynamicColor.themeColor
        continueBtn.backgroundColor = DynamicColor.themeColor
        mobileField.keyboardType = .numberPad
        self.verifyphoneNumberView.delegate = self
       // self.verifyphoneNumberView.setViewConfiguration(self.getConfigration())
        self.verifyphoneNumberView.setPhoneNumber("")
        verifyphoneNumberView.layer.cornerRadius = 1
        verifyphoneNumberView.layer.borderColor = UIColor.lightGray.cgColor
        verifyphoneNumberView.layer.borderWidth = 1
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
        Util.deactivateRTL(of: self.verifyphoneNumberView)
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            Util.deactivateRTL(of: self.verifyphoneNumberView)
        }
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "mobileSignupFieldPopup", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }

}

extension mobileSignupFieldPopup: JNPhoneNumberViewDelegate {
    
    /**
     Get presenter view controller
     - Parameter phoneNumberView: Phone number view
     - Returns: presenter view controller
     */
    func phoneNumberView(getPresenterViewControllerFor phoneNumberView: JNPhoneNumberView) -> UIViewController {
        return vendorRegistration!
    }
    
    /**
     Get country code picker attributes
     - Parameter phoneNumberView: Phone number view
     */
    func phoneNumberView(getCountryPickerAttributesFor phoneNumberView: JNPhoneNumberView) -> JNCountryPickerConfiguration {
        let configuration = JNCountryPickerConfiguration()
        configuration.pickerLanguage = .en
        configuration.tableCellInsets = UIEdgeInsets(top: 10.0, left: 10.0, bottom: 10.0, right: 10.0)
        configuration.viewBackgroundColor = UIColor.lightGray
        configuration.tableCellBackgroundColor = UIColor.white
        
        return configuration
    }
    
    /**
     Did change text
     - Parameter nationalNumber: National phone number
     - Parameter country: Number country info
     - Parameter phoneNumberView: Phone number view
     */
    func phoneNumberView(didChangeText nationalNumber: String, country: JNCountry, forPhoneNumberView phoneNumberView: JNPhoneNumberView) {
      //  self.phoneNumberLabel.text = "International Phone Number: \n \(self.phoneNumberView.getDialCode() + nationalNumber)"
       // self.verifyphoneNumberView.setViewConfiguration(self.getConfigration())
    }
    
    /**
     Did end editing
     - Parameter nationalNumber: National phone number
     - Parameter country: Number country info
     - Parameter isValidPhoneNumber:  Is valid phone number flag as bool
     - Parameter phoneNumberView: Phone number view
     */
    func phoneNumberView(didEndEditing nationalNumber: String, country: JNCountry, isValidPhoneNumber: Bool, forPhoneNumberView phoneNumberView: JNPhoneNumberView) {
        let validationMessage = isValidPhoneNumber ? "Valid Phone Number" : "Invalid Phone Number"
       // self.phoneNumberLabel.text = "International Phone Number: \n \(self.phoneNumberView.getDialCode() + nationalNumber) \n \(validationMessage)"
        
       // self.phoneNumberLabel.textColor = isValidPhoneNumber ? UIColor.blue : UIColor.red
    }
    
    /**
     Country Did Changed
     - Parameter country: New Selected Country
     - Parameter isValidPhoneNumber: Is valid phone number flag as bool
     - Parameter phoneNumberView: Phone number view
     */
    func phoneNumberView(countryDidChanged country: JNCountry, isValidPhoneNumber: Bool, forPhoneNumberView phoneNumberView: JNPhoneNumberView) {
        let validationMessage = isValidPhoneNumber ? "Valid Phone Number" : "Invalid Phone Number"
       // self.phoneNumberLabel.text = "International Phone Number: \n \(self.phoneNumberView.getPhoneNumber()) \n \(validationMessage)"
       // self.phoneNumberLabel.textColor = isValidPhoneNumber ? UIColor.blue : UIColor.red
    }
    
    private func getConfigration() -> JNPhoneNumberViewConfiguration {
        let configrartion = JNPhoneNumberViewConfiguration()
        configrartion.phoneNumberTitleColor = UIColor.white
        configrartion.countryDialCodeTitleColor = UIColor.white
        configrartion.phoneNumberTitleFont = UIFont.systemFont(ofSize: 15.0)
        configrartion.countryDialCodeTitleFont = UIFont.systemFont(ofSize: 20.0)
        return configrartion
    }
}
