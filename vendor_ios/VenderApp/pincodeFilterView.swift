//
//  pincodeFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 7/5/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class pincodeFilterView: UIView {
    
    @IBOutlet weak var lblZipcode: UILabel!
    @IBOutlet weak var zipcode: UITextField!
    
    @IBOutlet weak var lblcanship: UILabel!
    @IBOutlet weak var canShip: UIButton!
    
    @IBOutlet weak var lblCanCod: UILabel!
    @IBOutlet weak var Cod: UIButton!
    
    @IBOutlet weak var lbldelivery: UILabel!
    @IBOutlet weak var delivery: UITextField!
    
    @IBOutlet weak var applybttn: UIButton!
    @IBOutlet weak var resetbttn: UIButton!
    
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
        applybttn.setThemeColor()
        resetbttn.setThemeColor()
 
        lblZipcode.text = " ZipCode ".localized
        lblcanship.text = " Can Ship ".localized
        lblCanCod.text = " Can COD ".localized
        lbldelivery.text = " Day To Deliver ".localized
        applybttn.setTitle("Filter".localized, for: .normal)
        resetbttn.setTitle("Reset".localized, for: .normal)
        canShip.setTitle("----Select----".localized, for: .normal)
        Cod.setTitle("----Select----".localized, for: .normal)
        addSubview(view)
        view.cardView()
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "pincodeFilterView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

}
