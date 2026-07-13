//
//  zoneView.swift
//  VenderApp
//
//  Created by Manohar Singh Rawat on 29/09/20.
//  Copyright © 2020 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class zoneView: UIView {
    
    @IBOutlet weak var outerView: UIView!
    
    @IBOutlet weak var mainStackView: UIStackView!
    @IBOutlet weak var FromLabel: UILabel!    
    @IBOutlet weak var fromText: UITextField!
    
    @IBOutlet weak var toLabel: UILabel!
    @IBOutlet weak var toText: UITextField!
    
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var priceText: UITextField!
    
    @IBOutlet weak var typeLabel: UILabel!
    @IBOutlet weak var typeButton: UIButton!
    
    @IBOutlet weak var removeLabel: UILabel!
    @IBOutlet weak var removeBtn: UIButton!
    
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
        let nib = UINib(nibName: "zoneView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
}
