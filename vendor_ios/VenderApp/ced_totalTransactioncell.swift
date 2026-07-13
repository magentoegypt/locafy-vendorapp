//
//  ced_totalTransactioncell.swift
//  VenderApp
//
//  Created by Saumya Kashyap on 15/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_totalTransactioncell: UITableViewCell {

    @IBOutlet weak var totalAmountTitle: UILabel!
    @IBOutlet weak var totalServiceTitle: UILabel!
    @IBOutlet weak var totalAmountPay: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var totalServiceTax: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        totalServiceTitle.text=" Toatal Service Tax".localized
        totalAmountTitle.text=" Total Amount Pay".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
