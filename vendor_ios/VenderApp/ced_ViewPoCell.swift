//
//  ced_ViewPoCell.swift
//  VenderApp
//
//  Created by Macmini on 15/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ViewPoCell: UITableViewCell {
    
    
    
    @IBOutlet weak var cellview: UIView!
    
    
    @IBOutlet weak var po_increment_id: UILabel!
    
    
    
    @IBOutlet weak var QuoteId: UILabel!
    
    @IBOutlet weak var status: UILabel!
    @IBOutlet weak var QuoteIncrementId: UILabel!
    @IBOutlet weak var CreatedAt: UILabel!
    
    
    @IBOutlet weak var poIncrementLbl: UILabel!
    @IBOutlet weak var quoteIdLbl: UILabel!
    @IBOutlet weak var StatusLbl: UILabel!
    @IBOutlet weak var quoteIncrementIdLbl: UILabel!
    @IBOutlet weak var createdAtLbl: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
         contentView.makeCard(cellview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        poIncrementLbl.text = "PO Increment Id".localized
        quoteIdLbl.text = "Quote Id".localized
        StatusLbl.text = "Status".localized
        quoteIncrementIdLbl.text = "Quote Increment Id".localized
        createdAtLbl.text = "Created At".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

}
