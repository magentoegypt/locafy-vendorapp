//
//  ced_ManageQuotesCell.swift
//  VenderApp
//
//  Created by Macmini on 13/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ManageQuotesCell: UITableViewCell {
    
    
    @IBOutlet weak var contenerview: UIView!
    
    @IBOutlet weak var quoteId: UILabel!
    @IBOutlet weak var QuoteIncrementId: UILabel!
    @IBOutlet weak var CreatedAt: UILabel!
   
    @IBOutlet weak var CustomerEmail: UILabel!
    @IBOutlet weak var Status: UILabel!
   
    
    @IBOutlet weak var QuoteIdLbl: UILabel!
    @IBOutlet weak var quoteIncrementLbl: UILabel!
    @IBOutlet weak var CreatedAtLbl: UILabel!
    @IBOutlet weak var customerEmailLbl: UILabel!
    @IBOutlet weak var StatusLbl: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        contenerview.makeCard(contenerview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        QuoteIdLbl.text = "Quote Id".localized
        quoteIncrementLbl.text = "Quote Increment Id".localized
        CreatedAtLbl.text = "Created At".localized
        customerEmailLbl.text = "Customer Email".localized
        StatusLbl.text = "Status".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

}
