//
//  ced_itemOrdered.swift
//  VenderApp
//
//  Created by Macmini on 15/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_itemOrdered: UITableViewCell {

    
    
    
    @IBOutlet weak var ced_itemOrderedCell: UIView!
    
    
    
    @IBOutlet weak var SKU: UILabel!
    @IBOutlet weak var product: UILabel!
    @IBOutlet weak var ItemStock: UILabel!
   
    @IBOutlet weak var requestedQty: UILabel!
    
    @IBOutlet weak var toplable: UILabel!
    
    @IBOutlet weak var requestedUniPrice: UILabel!
    
    @IBOutlet weak var proposedQty: UITextField!
    
    @IBOutlet weak var proposalCreatedForQty: UILabel!
    
    @IBOutlet weak var proposedUnitPrice: UITextField!
    
    @IBOutlet weak var rowTotal: UILabel!
    
    
    @IBOutlet weak var prodLbl: UILabel!
    @IBOutlet weak var skuLbl: UILabel!
    @IBOutlet weak var stockLbl: UILabel!
    @IBOutlet weak var requestedQtyLbl: UILabel!
    @IBOutlet weak var requestedUnitPriceLbl: UILabel!
    @IBOutlet weak var proposedQtyLbl: UILabel!
    @IBOutlet weak var proposalCreatedLbl: UILabel!
    @IBOutlet weak var proposalPriceLbl: UILabel!
    @IBOutlet weak var rowTotalLbl: UILabel!
    
    
    @IBOutlet weak var cellview: UIView!
    
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        prodLbl.text = "Product".localized
        skuLbl.text = "SKU".localized
        stockLbl.text = "Item Stock".localized
        requestedQtyLbl.text = "Requested Qty".localized
        requestedUnitPriceLbl.text = "Requested Unit Price".localized
        proposedQtyLbl.text = "Proposed Qty".localized
        proposalCreatedLbl.text = "Proposal Created for Qty".localized
        proposalPriceLbl.text = "Proposed Unit Price".localized
        rowTotalLbl.text = "Row Total".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
