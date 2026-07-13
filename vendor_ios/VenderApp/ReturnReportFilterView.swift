//
//  ReturnReportFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ReturnReportFilterView: UIView {

    @IBOutlet weak var headingLabel: UIButton!
    @IBOutlet weak var orderId: UITextField!
    
    @IBOutlet weak var orderDate: UITextField!
    
    @IBOutlet weak var status: UIButton!
    
    @IBOutlet weak var applyFilter: UIButton!
    
    
    @IBOutlet weak var clearButton: UIButton!
    
    @IBOutlet weak var orderIdLbl: UILabel!
    @IBOutlet weak var orderDateLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
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
        clearButton.setThemeColor()
        orderIdLbl.text = "Order Id".localized
        orderDateLbl.text = "Order Date".localized
        statusLbl.text = "Status".localized
        status.setTitle("Select Status".localized, for: .normal)
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ReturnReportFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        applyFilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        clearButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

}
