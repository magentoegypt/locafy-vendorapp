//
//  ced_FilterXib_ManageQuotes.swift
//  VenderApp
//
//  Created by Macmini on 17/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_FilterXib_ManageQuotes: UIView {
    
    
    
    @IBOutlet weak var resetfilter: UIButton!
    @IBOutlet weak var filter: UIButton!
    
    @IBOutlet weak var QuoteIncrementId: UITextField!
    
    
    @IBOutlet weak var fromQuoteId: UITextField!
    
    @IBOutlet weak var custumeremail: UITextField!
    
    
    @IBOutlet weak var fromCreatedAt: UITextField!
    
    
    @IBOutlet weak var toCreatedAt: UITextField!
    @IBOutlet weak var Status: UIButton!
    
    @IBOutlet weak var quoteIncIdLbl: UILabel!
    @IBOutlet weak var createdAtLbl: UILabel!
    @IBOutlet weak var customerNameLvl: UILabel!
    @IBOutlet weak var quoteIdLvl: UILabel!
    @IBOutlet weak var statusLvl: UILabel!
    
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
        
        Status.setTitle("Select Status".localized, for: .normal)
        quoteIncIdLbl.text = "Quote Increment Id".localized
        createdAtLbl.text = "Created At".localized
        toCreatedAt.placeholder = "To".localized
        fromCreatedAt.placeholder = "From".localized
        customerNameLvl.text = "Customer Email".localized
        quoteIdLvl.text = "Quote Id".localized
//        fromQuoteId.placeholder = "From".localized
        statusLvl.text = "Status".localized
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ced_FilterXib_ManageQuotes", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        filter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        resetfilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

    
}
