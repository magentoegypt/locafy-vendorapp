//
//  cedPaymentListCell.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedPaymentListCell: UITableViewCell {

    @IBOutlet weak var orderDescription: UILabel!
    
    @IBOutlet weak var transactionDate: UILabel!
    
    @IBOutlet weak var transactionId: UILabel!
    
    @IBOutlet weak var creditAmount: UILabel!
    
    @IBOutlet weak var status: UILabel!
    
    @IBOutlet weak var cellView: UIView!
    
    @IBOutlet weak var orderDescLbl: UILabel!
    @IBOutlet weak var transactionDateLbl: UILabel!
    @IBOutlet weak var transactionIdLbl: UILabel!
    @IBOutlet weak var creditAmountLbl: UILabel!
    @IBOutlet weak var statusLbl: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        orderDescLbl.text = "Order Description".localized
        transactionDateLbl.text = "Transaction Date".localized
        transactionIdLbl.text = "Transaction Id".localized
        creditAmountLbl.text = "Credit Amount".localized
        statusLbl.text = "Status".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
