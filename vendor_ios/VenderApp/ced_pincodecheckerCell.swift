//
//  ced_pincodecheckerCell.swift
//  VenderApp
//
//  Created by cedcoss on 7/5/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_pincodecheckerCell: UITableViewCell {

    
    @IBOutlet weak var mainview: UIView!
    @IBOutlet weak var mainstack: UIStackView!
    @IBOutlet weak var lblzipcode: UILabel!
    @IBOutlet weak var zipcode: UILabel!
    @IBOutlet weak var lblcanship: UILabel!
    @IBOutlet weak var canship: UILabel!
    @IBOutlet weak var lblcanCOD: UILabel!
    @IBOutlet weak var Cod: UILabel!
    @IBOutlet weak var lbldelivery: UILabel!
    
    @IBOutlet weak var delivery: UILabel!
    @IBOutlet weak var AddBttn: UIButton!
    var data = JSON()
    override func awakeFromNib() {
        super.awakeFromNib()
        mainview.cardView()
        lblzipcode.text = " ZipCode ".localized
        lblcanship.text = " Can Ship ".localized
        lblcanCOD.text = " Can COD ".localized
        lbldelivery.text = " Day To Deliver ".localized
        
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func populate()
    {
        zipcode.text = data["zipcode"].stringValue
        if data["can_ship"].stringValue == "1"
        {
            canship.text = "Yes".localized
        }
         else
        {
            canship.text = "No".localized
        }
        if data["can_cod"].stringValue == "1"
        {
            Cod.text = "Yes".localized
        }
         else
        {
            Cod.text = "No".localized
        }
          
        delivery.text = data["days_to_deliver"].stringValue
         
        AddBttn.backgroundColor = DynamicColor.themeColor
        AddBttn.setTitleColor(.white, for: .normal)
    }

}
