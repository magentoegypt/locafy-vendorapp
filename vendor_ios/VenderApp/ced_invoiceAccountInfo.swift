//
//  ced_invoiceAccountInfo.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 30/06/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_invoiceAccountInfo: UITableViewCell {

    @IBOutlet weak var cName: UILabel!
    @IBOutlet weak var cgrp: UILabel!
    @IBOutlet weak var cemail: UILabel!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var customerName: UILabel!
    
    @IBOutlet weak var customergroup: UILabel!
    @IBOutlet weak var email: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        if(topLabel != nil){
        topLabel.backgroundColor = color
        }
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
       
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }

}
