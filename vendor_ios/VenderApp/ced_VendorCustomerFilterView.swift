//
//  ced_VendorCustomerFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 10/05/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import Foundation
class ced_VendorCustomerFilterView: UIView {
    
    @IBOutlet weak var vendorAppBtn: UIButton!
    @IBOutlet weak var senderField: UITextField!
    @IBOutlet weak var toCraetedAt: UITextField!
    @IBOutlet weak var subject: UITextField!
    
    @IBOutlet weak var resetBtn: UIButton!
    @IBOutlet weak var filterBtn: UIButton!
    @IBOutlet weak var toUpdatedAt: UITextField!
    @IBOutlet weak var fromUpdatedAt: UITextField!
    @IBOutlet weak var fromCreatedAt: UITextField!
    
    @IBOutlet weak var recieverField: UITextField!
    
    @IBOutlet weak var senderLbl: UILabel!
    @IBOutlet weak var recieverLbl: UILabel!
    @IBOutlet weak var createdAtLbl: UILabel!
    @IBOutlet weak var updatedAtLbl: UILabel!
    @IBOutlet weak var subjectLbl: UILabel!
    
    
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
        
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        vendorAppBtn.setTitle("Vendor App".localized, for: .normal)
        senderLbl.text = "Subject".localized
        recieverLbl.text = "Receiver".localized
        createdAtLbl.text = "Created At".localized
        updatedAtLbl.text = "Updated At".localized
        subjectLbl.text = "Subject".localized
        fromCreatedAt.placeholder = "From".localized
        toCraetedAt.placeholder = "To".localized
        toUpdatedAt.placeholder = "To".localized
        fromUpdatedAt.placeholder = "From".localized
        
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ced_VendorCustomerFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
}
