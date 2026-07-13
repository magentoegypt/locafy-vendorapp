//
//  ced_RmaCustomerInformation.swift
//  VenderApp
//
//  Created by Macmini on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_RmaCustomerInformation: UITableViewCell {

    @IBOutlet weak var toplable: UILabel!
    @IBOutlet weak var CustomerName: UITextField!
    @IBOutlet weak var CustomerEmail: UITextField!
    @IBOutlet weak var CustomerGroup: UITextField!
    
    @IBOutlet weak var CustomerAddress: UILabel!

    @IBOutlet weak var customerNameLbl: UILabel!
    @IBOutlet weak var customerEmailLbl: UILabel!
    @IBOutlet weak var customergrpLbl: UILabel!
    @IBOutlet weak var customerAddLbl: UILabel!
    
    @IBOutlet weak var cellview: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        cellview.makeCard(cellview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        toplable.text = "Customer Information ".localized
        customerNameLbl.text = "Customer Name: ".localized
        customerEmailLbl.text = "Customer Email: ".localized
        customergrpLbl.text = "Customer Group: ".localized
        customerAddLbl.text = "Customer Address: ".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
