//
//  bidsFilterView.swift
//  VenderApp
//
//  Created by MacMini on 24/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class bidsFilterView: UIView {

    @IBOutlet weak var id: UITextField!
    
    @IBOutlet weak var productId: UITextField!
    
    @IBOutlet weak var customerId: UITextField!
    
    @IBOutlet weak var customerName: UITextField!
    
    @IBOutlet weak var bidPrice: UITextField!
    
    @IBOutlet weak var bidDate: UITextField!
    
    @IBOutlet weak var bidTime: UITextField!
    
    @IBOutlet weak var winner: UITextField!
    
    @IBOutlet weak var status: UITextField!
    
    @IBOutlet weak var applyButton: UIButton!
    
    @IBOutlet weak var clearButton: UIButton!
    
    
    
    
    
    
    
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
        
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "bidsFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        
        var colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        applyButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        clearButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }

}
