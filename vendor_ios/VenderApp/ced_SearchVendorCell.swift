//
//  ced_SearchVendorCell.swift
//  VenderApp
//
//  Created by cedcoss on 7/20/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_SearchVendorCell: UITableViewCell {
    
    @IBOutlet weak var Lblsearch: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        print("search is active")
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
