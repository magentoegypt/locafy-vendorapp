//
//  ReviewFilterview.swift
//  VenderApp
//
//  Created by cedcoss on 4/7/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ReviewFilterview: UIView {
    
    @IBOutlet weak var Toplabel: UILabel!
    
    @IBOutlet weak var lablId: UILabel!
    @IBOutlet weak var id: UITextField!
     
    @IBOutlet weak var Lblcreated: UILabel!
    @IBOutlet weak var bttnFrom: UIButton!
    @IBOutlet weak var bttnuntil: UIButton!
    
    @IBOutlet weak var lblsituation: UILabel!
    @IBOutlet weak var bttnsituation: UIButton!
    @IBOutlet weak var lbltitle: UILabel!
    @IBOutlet weak var title: UITextField!
    
  
    @IBOutlet weak var lblsurname: UILabel!
    @IBOutlet weak var surname: UITextField!
    
    @IBOutlet weak var lblevaluation: UILabel!
    @IBOutlet weak var evaluation: UITextField!
    
    @IBOutlet weak var lblvisibility: UILabel!
    @IBOutlet weak var bttnvisibility: UIButton!
    @IBOutlet weak var lbltype: UILabel!
    @IBOutlet weak var bttntype: UIButton!
    @IBOutlet weak var lblproduct: UILabel!
    @IBOutlet weak var product: UITextField!
    
    @IBOutlet weak var lblcode: UILabel!
 
    @IBOutlet weak var code: UITextField!
 
    @IBOutlet weak var bttapply: UIButton!
    @IBOutlet weak var bttnclear: UIButton!
    
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
        bttapply.setThemeColor()
        bttnclear.setThemeColor()
        Toplabel.setThemeColor()
        if(ced_storeVC.selectLangauge == "ar"){
            lablId.textAlignment = .right
            Lblcreated.textAlignment = .right
            lblsituation.textAlignment = .right
            lbltitle.textAlignment = .right
            lblevaluation.textAlignment = .right
            lblvisibility.textAlignment = .right
            lbltype.textAlignment = .right
            lblproduct.textAlignment = .right
            lblcode.textAlignment = .right
        }
        addSubview(view)
        view.cardView()
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ReviewFilterview", bundle: bundle)
        
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
