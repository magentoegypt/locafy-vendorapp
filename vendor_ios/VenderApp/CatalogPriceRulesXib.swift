//
//  CatalogPriceRulesXib.swift
//  VenderApp
//
//  Created by Macmini on 11/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class CatalogPriceRulesXib: UIView {
    
    
    
    @IBOutlet weak var topheader: UILabel!
    
    
    @IBOutlet weak var id: UITextField!
    
    @IBOutlet weak var rule: UITextField!
    
    @IBOutlet weak var website: UIButton!
    
    @IBOutlet weak var startto: UITextField!
    
    @IBOutlet weak var startfrom: UITextField!
    
    @IBOutlet weak var endfrom: UITextField!
    
    @IBOutlet weak var endto: UITextField!
    
    @IBOutlet weak var resetbutton: UIButton!
    
    @IBOutlet weak var status: UIButton!
    
    
    @IBOutlet weak var filterbutton: UIButton!
    
    
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
        addSubview(view)
    }
    
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "CatalogPriceRulesXib", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }

}
