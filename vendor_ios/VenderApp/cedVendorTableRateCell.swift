//
//  cedVendorTableRateCell.swift
//  VenderApp
//
//  Created by cedcoss on 22/12/17.
//  Copyright © 2017 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedVendorTableRateCell: UITableViewCell {

    @IBOutlet weak var price: UILabel!
    @IBOutlet weak var conditionValue: UILabel!
    @IBOutlet weak var conditionCode: UILabel!
    @IBOutlet weak var zipCode: UILabel!
    @IBOutlet weak var destinationreregionId: UILabel!
    @IBOutlet weak var destinationcountryId: UILabel!
    @IBOutlet weak var id: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
