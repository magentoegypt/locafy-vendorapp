//
//  ced_CreatePOitemCell.swift
//  VenderApp
//
//  Created by Macmini on 26/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_CreatePOitemCell: UITableViewCell {
    @IBOutlet weak var cellview: UIView!
    @IBOutlet weak var headerinfo: UILabel!
    @IBOutlet weak var Product: UILabel!
    @IBOutlet weak var SKU: UILabel!
    
    @IBOutlet weak var RowTotal: UILabel!
    @IBOutlet weak var requestedUnitPrice: UILabel!
    
    @IBOutlet weak var proposedQty: UITextField!
    @IBOutlet weak var proposedUnitPrice: UILabel!
    @IBOutlet weak var proposalCreatedForQty: UILabel!
    
    
    @IBOutlet weak var ProductLbl: UILabel!
    @IBOutlet weak var SKUlbl: UILabel!
    @IBOutlet weak var ProposedQtyLbl: UILabel!
    @IBOutlet weak var creaatedProposalLbl: UILabel!
    @IBOutlet weak var proposedPriceLbl: UILabel!
    @IBOutlet weak var requestedPriceLbl: UILabel!
    @IBOutlet weak var rowTotalLbl: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        headerinfo.text = "ITEMS ORDERED".localized
        ProductLbl.text = "Product".localized
        SKUlbl.text = "SKU".localized
        ProposedQtyLbl.text = "Proposed Qty".localized
        creaatedProposalLbl.text = "Proposal Created For Qty".localized
        proposedPriceLbl.text = "Proposed Unit Price".localized
        requestedPriceLbl.text = "Requested Unit Price".localized
        rowTotalLbl.text = "Row Total".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
