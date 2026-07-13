//
//  CmsPageFilter.swift
//  VenderApp
//
//  Created by cedcoss on 13/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class CmsPageFilter: UIView {
    
    // Our custom view from the XIB file
    var view: UIView!
    
    @IBOutlet weak var topSpace: NSLayoutConstraint!
    
    @IBOutlet weak var idWidth: NSLayoutConstraint!
    @IBOutlet weak var value11width: NSLayoutConstraint!
    @IBOutlet weak var label7Height:  NSLayoutConstraint!
    @IBOutlet weak var value11Height: NSLayoutConstraint!
    //outlets
    @IBOutlet weak var ButtonTopSpace: UIView!
    
    @IBOutlet weak var LayoutHeibht: NSLayoutConstraint!
    
    @IBOutlet weak var LayoutButton: NSLayoutConstraint!
    
    @IBOutlet weak var coreWrapperView: UIView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var label1: UILabel!
    @IBOutlet weak var value11: UITextView!
    @IBOutlet weak var value12: UITextView!
    @IBOutlet weak var label2: UILabel!
    @IBOutlet weak var value21: UITextView!
    @IBOutlet weak var value22: UITextView!
    @IBOutlet weak var label3: UILabel!
    @IBOutlet weak var value31: UITextView!
    @IBOutlet weak var value32: UITextView!
    @IBOutlet weak var label4: UILabel!
    @IBOutlet weak var value4: UITextView!
    @IBOutlet weak var label5: UILabel!
    @IBOutlet weak var value5: UITextView!
    @IBOutlet weak var label6: UILabel!
    @IBOutlet weak var value6: UIButton!
    @IBOutlet weak var label7: UILabel!
    @IBOutlet weak var value7: UIButton!
    @IBOutlet weak var leftButton: UIButton!
    @IBOutlet weak var rightButton: UIButton!
    @IBOutlet weak var datepicker: UIDatePicker!
    
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
        let color = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        let col = Ced_CommonVendor.UIColorFromRGB(color)
        
      //  topLabel.backgroundColor = col
        topLabel.text = "Vendor App".localized
        leftButton.backgroundColor = col
        rightButton.backgroundColor = col
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "CmsPageFilter", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    
}
