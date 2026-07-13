//
//  ced_invoicelistCell.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 30/06/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_invoicelistCell: UITableViewCell {
    @IBOutlet weak var inDateTitle: UILabel!
    @IBOutlet weak var inTitle: UILabel!
    @IBOutlet weak var inStatus: UILabel!
    @IBOutlet weak var inOrder: UILabel!
    @IBOutlet weak var inOrderdate2: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var invoice: UILabel!
    @IBOutlet weak var invoiceDate: UILabel!
    @IBOutlet weak var order: UILabel!
    @IBOutlet weak var orderdate: UILabel!
    @IBOutlet weak var Billtoname: UILabel!
    @IBOutlet weak var status: UILabel!
    @IBOutlet weak var amount: UILabel!
    @IBOutlet weak var inAmount: UILabel!
    @IBOutlet weak var billTitle: UILabel!
    
    
    @IBOutlet weak var view2: UIView!
    @IBOutlet weak var view1: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        inDateTitle.text="Invoice Date".localized
        inTitle.text="Invoice #".localized
        inStatus.text="Status".localized
        inOrder.text="Order #".localized
        inOrderdate2.text="Order Date".localized
        inAmount.text="Amount".localized
        billTitle.text="Bill to Name".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
