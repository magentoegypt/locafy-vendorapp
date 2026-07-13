//
//  ced_invoiceproduct.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 30/06/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_invoiceproduct: UITableViewCell {

    @IBOutlet weak var inVoicedQty: UILabel!
    @IBOutlet weak var shippedQty: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var productName: UILabel!
    @IBOutlet weak var price: UILabel!
    @IBOutlet weak var sku: UILabel!
    @IBOutlet weak var qty: UILabel!
    @IBOutlet weak var subtotal: UILabel!
    @IBOutlet weak var taxamount: UILabel!
    @IBOutlet weak var discountAmount: UILabel!
    @IBOutlet weak var rowTotal: UILabel!
    
    @IBOutlet weak var taxPercentLabel: UILabel!
    
    @IBOutlet weak var additionalStackHeight: NSLayoutConstraint!
    
    @IBOutlet weak var additionalStackView: UIStackView!
    @IBOutlet weak var igstLabel: UILabel!
    @IBOutlet weak var originalPriceLabel: UILabel!
    @IBOutlet weak var additionalInfoStackHeight: NSLayoutConstraint!
    @IBOutlet weak var additionalInfoStackView: UIStackView!
    
    @IBOutlet weak var cgstLabel: UILabel!
    @IBOutlet weak var sgstLabel: UILabel!
    @IBOutlet weak var itemStatusLabel: UILabel!
    @IBOutlet weak var qtyToInvoice: UITextField!
    @IBOutlet weak var returnTostock: DLRadioButton!
    
    @IBOutlet weak var prodNameLbl: UILabel!
    @IBOutlet weak var itemStatus: UILabel!
    @IBOutlet weak var orignalPrice: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var skuLbl: UILabel!
    @IBOutlet weak var qtyLbl: UILabel!
    @IBOutlet weak var subtotalLbl: UILabel!
    @IBOutlet weak var taxAmmLbl: UILabel!
    @IBOutlet weak var discountLbl: UILabel!
    @IBOutlet weak var taxPercentLbl: UILabel!
    @IBOutlet weak var rowTotalLbl: UILabel!
    
    var dataCheck = true;
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        if(returnTostock != nil){
            returnTostock.isMultipleSelectionEnabled = true
        }
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
