//
//  ced_shipmentcell.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 01/07/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_shipmentcell: UITableViewCell {

    @IBOutlet weak var idTitle: UILabel!
    @IBOutlet weak var iddate: UILabel!
    @IBOutlet weak var shipToTitle: UILabel!
    @IBOutlet weak var shipmentQty: UILabel!
    @IBOutlet weak var view2: UIView!
    @IBOutlet weak var view1: UIView!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var shipmentId: UILabel!
    @IBOutlet weak var dateShipped: UILabel!
    @IBOutlet weak var orderId: UILabel!
    @IBOutlet weak var orderDate: UILabel!
    @IBOutlet weak var shipToname: UILabel!
    @IBOutlet weak var shqty: UILabel!
    @IBOutlet weak var sd: UILabel!
    @IBOutlet weak var sid: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        view1.setThemeColor()
        view2.setThemeColor()
        // Configure the view for the selectedstate
        idTitle.text="Order #".localized
        iddate.text="Order Date".localized
        shipToTitle.text="Ship To Name".localized
        shqty.text="Ship Qty".localized
        sd.text="Shipment Date".localized
        sid.text="Shipment Id".localized
    }

}
