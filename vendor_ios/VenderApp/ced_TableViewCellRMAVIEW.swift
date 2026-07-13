//
//  ced_TableViewCellRMAVIEW.swift
//  VenderApp
//
//  Created by Macmini on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_TableViewCellRMAVIEW: UITableViewCell {
    
    
    @IBOutlet weak var PurchasedPoint: UILabel!
    
    @IBOutlet weak var OrderId: UILabel!
    
    
    @IBOutlet weak var RMAId: UILabel!
    
    @IBOutlet weak var CustomerName: UILabel!
    
    @IBOutlet weak var CustomerEmail: UILabel!
    
    @IBOutlet weak var Status: UILabel!
    
    @IBOutlet weak var ResolutionRequested: UILabel!
    @IBOutlet weak var ContenerView: UIView!
    
    @IBOutlet weak var UpdatedAt: UILabel!
    @IBOutlet weak var purchasedPointLbl: UILabel!
    @IBOutlet weak var orderIdLbl: UILabel!
    @IBOutlet weak var rmaIdLbl: UILabel!
    @IBOutlet weak var customerNameLbl: UILabel!
    @IBOutlet weak var customerEmailLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
    @IBOutlet weak var resolutionLbl: UILabel!
    @IBOutlet weak var updatedLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        purchasedPointLbl.text = "Purchased Point".localized
        orderIdLbl.text = "Order Id".localized
        rmaIdLbl.text = "RMA Id".localized
        customerNameLbl.text = "Customer Name".localized
        customerEmailLbl.text = "Customer Email".localized
        statusLbl.text = "Status".localized
        resolutionLbl.text = "Resolution Requested".localized
        updatedLbl.text = "Updated At".localized
        
        contentView.makeCard(ContenerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
