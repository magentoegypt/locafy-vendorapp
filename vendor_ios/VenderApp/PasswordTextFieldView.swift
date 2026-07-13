//
//  PasswordTextFieldView.swift
//  VenderApp
//
//  Created by Manohar Singh Rawat on 13/12/19.
//  Copyright © 2019 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class PasswordTextFieldView: UIView {

    @IBOutlet weak var headingLabel: UILabel!
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    @IBOutlet weak var showHideBtn: UIButton!
    
    
    
    var view: UIView!
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
        passwordTextField.layer.borderWidth = 1.0;
       // passwordTextField.delegate = self
        passwordTextField.isSecureTextEntry = true;
        passwordTextField.layer.borderColor = UIColor.black.cgColor;
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "PasswordTextFieldView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }

}
