//
//  PaymentReportFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class PaymentReportFilterView: UIView {

    @IBOutlet weak var headingLabel: UIButton!
    
    @IBOutlet weak var transactionDateFrom: UITextField!
    
    @IBOutlet weak var transactionTo: UITextField!
    
    @IBOutlet weak var transactionId: UITextField!
    
    @IBOutlet weak var creditAmountFrom: UITextField!
    
    @IBOutlet weak var creditAmountTo: UITextField!
    
    @IBOutlet weak var applyFilter: UIButton!
    
    @IBOutlet weak var clearButton: UIButton!
    
    @IBOutlet weak var transactionDateLbl: UILabel!
    @IBOutlet weak var transactionIdLbl: UILabel!
    @IBOutlet weak var creditAmountLbl: UILabel!
    var view: UIView!;
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
        headingLabel.setThemeColor()
        headingLabel.setTitle("Vendor App".localized, for: .normal)
        applyFilter.setThemeColor()
        clearButton.setThemeColor();
        creditAmountLbl.text = "Credit Amount".localized
        creditAmountTo.placeholder = "To".localized
        creditAmountFrom.placeholder = "From".localized
        transactionIdLbl.text = "Transaction ID".localized
        transactionTo.placeholder = "To".localized
        transactionDateFrom.placeholder = "From".localized
        transactionDateLbl.text = "Transaction Date".localized
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "PaymentReportFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        applyFilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        clearButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

}
