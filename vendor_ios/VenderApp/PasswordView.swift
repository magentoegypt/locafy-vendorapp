//
//  PasswordView.swift
//  VenderApp
//
//  Created by Manohar Singh Rawat on 02/03/19.
//  Copyright © 2019 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class PasswordView: UIView {

    var view = UIView()
    
    @IBOutlet weak var currentPasswordLabel: UILabel!
    
    @IBOutlet weak var currentPasswordField: UITextField!
    
    @IBOutlet weak var newPasswordLabel: UILabel!
    
    @IBOutlet weak var newPasswordField: UITextField!
    
    @IBOutlet weak var confirmPasswordLabel: UILabel!
    
    @IBOutlet weak var confirmPasswordField: UITextField!
    @IBOutlet weak var currentPasswordbtn: UIButton!
    @IBOutlet weak var newPasswordbtn: UIButton!
    @IBOutlet weak var confirmPasswordbtn: UIButton!
    
    @IBOutlet weak var passwordStrengthLabel: UILabel!
    override init(frame: CGRect)
    {
        // 1. setup any properties here
        
        // 2. call super.init(frame:)
        super.init(frame: frame)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    required init?(coder aDecoder: NSCoder)
    {
        // 1. setup any properties here
        
        // 2. call super.init(coder:)
        super.init(coder: aDecoder)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    @objc func xibSetup()
    {
        view = loadViewFromNib()
        
        // use bounds not frame or it'll be offset
        view.frame = bounds
        
        // Make the view stretch with containing view
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
        
        //designing code
        
        //designing code
        
        if(currentPasswordbtn != nil){
            currentPasswordbtn.setImage(UIImage(named: "eye_hide"), for: .normal)
            newPasswordbtn.setImage(UIImage(named: "eye_hide"), for: .normal)
            confirmPasswordbtn.setImage(UIImage(named: "eye_hide"), for: .normal)
        }
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "PasswordView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    @IBAction func currentShowhidetpd(_ sender: UIButton) {
        if sender.currentImage == UIImage(named: "eye_hide")
        {
            currentPasswordField.isSecureTextEntry = false;
            sender.setImage(UIImage(named: "eye"), for: .normal)
        }
        else
        {
            sender.setImage(UIImage(named: "eye_hide"), for: .normal)
            currentPasswordField.isSecureTextEntry = true;
        }
    }
    
    @IBAction func newShowhidetpd(_ sender: UIButton) {
        if sender.currentImage == UIImage(named: "eye_hide")
        {
            newPasswordField.isSecureTextEntry = false;
            sender.setImage(UIImage(named: "eye"), for: .normal)
        }
        else
        {
            sender.setImage(UIImage(named: "eye_hide"), for: .normal)
            newPasswordField.isSecureTextEntry = true;
        }
    }
    
    @IBAction func confirmShowhidetpd(_ sender: UIButton) {
        if sender.currentImage == UIImage(named: "eye_hide")
        {
            confirmPasswordField.isSecureTextEntry = false;
            sender.setImage(UIImage(named: "eye"), for: .normal)
        }
        else
        {
            sender.setImage(UIImage(named: "eye_hide"), for: .normal)
            confirmPasswordField.isSecureTextEntry = true;
        }
    }
    

}
