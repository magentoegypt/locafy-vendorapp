//
//  ced_RmaOrder Information.swift
//  VenderApp
//
//  Created by Macmini on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedRmaOrderInformation: UITableViewCell {
    
    @IBOutlet weak var toplabel: UILabel!
    @IBOutlet weak var orderid: UITextField!
    @IBOutlet weak var PurchaseFrom: UILabel!
    @IBOutlet weak var Status: UIButton!
    @IBOutlet weak var Reason: UITextField!
    @IBOutlet weak var PackageCondition: UITextField!
    @IBOutlet weak var ResolutionRequested: UITextField!
    
    @IBOutlet weak var cellview: UIView!
    
    @IBOutlet weak var orderidLbl: UILabel!
    @IBOutlet weak var purchasedFromLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
    @IBOutlet weak var reasonLbl: UILabel!
    @IBOutlet weak var packageConditionLvl: UILabel!
    @IBOutlet weak var resolutionReqLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        Reason.isEnabled=false
        orderid.isEnabled=false
        Reason.isEnabled=false
        ResolutionRequested.isEnabled=false
        purchasedFromLbl.text = "Purchase From".localized
        orderidLbl.text = "Order Id".localized
        statusLbl.text = "Status".localized
        reasonLbl.text = "Reason".localized
        packageConditionLvl.text = "Package Condition".localized
        resolutionReqLbl.text = "Resolution Requested".localized
        if(ced_storeVC.selectLangauge == "ar"){
            Status.contentHorizontalAlignment = .right
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
 cellview.makeCard(cellview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        // Configure the view for the selected state
    }

}
