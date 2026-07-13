//
//  RatingFilterView.swift
//  VenderApp
//
//  Created by cedcoss on 4/22/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class RatingFilterView: UIView {
    
    @IBOutlet weak var toplabel: UILabel!
    
    @IBOutlet weak var bttnapply: UIButton!
    
    @IBOutlet weak var bttnclear: UIButton!
    @IBOutlet weak var lblid: UILabel!
    @IBOutlet weak var id: UITextField!
    @IBOutlet weak var lblclassification: UILabel!
    @IBOutlet weak var classification: UITextField!
    @IBOutlet weak var lblordering: UILabel!
    @IBOutlet weak var ordering: UITextField!
    @IBOutlet weak var lblisactive: UILabel!
    @IBOutlet weak var isactive: UITextField!
    
    
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
        bttnapply.setThemeColor()
        bttnclear.setThemeColor()
        toplabel.setThemeColor()
        addSubview(view)
        view.cardView()
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "RatingFilterView", bundle: bundle)
        
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
