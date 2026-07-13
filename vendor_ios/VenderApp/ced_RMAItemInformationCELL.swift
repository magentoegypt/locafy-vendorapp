//
//  ced_RMAItemInformationCELL.swift
//  VenderApp
//
//  Created by Macmini on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_RMAItemInformationCELL: UITableViewCell {
    
    @IBOutlet weak var toplable: UILabel!
    
    @IBOutlet weak var Product: UITextField!
    
    @IBOutlet weak var ItemSKU: UITextField!
    @IBOutlet weak var Price: UITextField!
    
    @IBOutlet weak var QtyforRMA: UITextField!
    
    @IBOutlet weak var rowTotal: UITextField!
    
    @IBOutlet weak var cellview: UIView!
    @IBOutlet weak var productLabel: UILabel!
    @IBOutlet weak var itemSkuLbl: UILabel!
    @IBOutlet weak var priceLvl: UILabel!
    
    @IBOutlet weak var rowTtlLbl: UILabel!
    @IBOutlet weak var qtyLvl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        cellview.makeCard(cellview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        toplable.text = "Item Information ".localized
        productLabel.text = "Product".localized
        itemSkuLbl.text = "SKU".localized
        priceLvl.text = "Price".localized
        qtyLvl.text = "Qty for RMA".localized
        rowTtlLbl.text = "Row Total".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
}
