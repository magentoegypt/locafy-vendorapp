//
//  browseImageView.swift
//  VenderApp
//
//  Created by MacMini on 20/02/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class browseImageView: UIView {
    @IBOutlet weak var browseImage: UIImageView!
    
    // Our custom view from the XIB file
    var view: UIView!
    
    //outlets
    
    @IBOutlet weak var fileAttachedLabel: UILabel!
    @IBOutlet weak var nameLabel: UIButton!
    @IBOutlet weak var browseButton: UIButton!
    var base64EncodedStringData=""
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
        browseButton.setTitle("Browse...".localized, for: .normal)
        nameLabel.setTitle("Download File".localized, for: .normal)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "browseImageView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }

}
