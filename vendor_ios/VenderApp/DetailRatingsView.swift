//
//  DetailRatingsView.swift
//  driverApp
//
//  Created by cedcoss on 9/20/21.
//  Copyright © 2021 Saumya Kashyap. All rights reserved.
//

import UIKit
import Cosmos

class DetailRatingsView: UIView {

    
    @IBOutlet weak var RatingLbl: UILabel!
    @IBOutlet weak var ratingView: CosmosView!
    
    var key = ""
    var view: UIView!;
    var minVal = Int()
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
        
  
        view.frame = bounds
        
 
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "DetailRatingsView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }

}
