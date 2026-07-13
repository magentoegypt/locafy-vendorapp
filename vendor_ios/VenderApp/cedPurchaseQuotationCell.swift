//
//  cedPurchaseQuotationCell.swift
//  VenderApp
//
//  Created by cedcoss on 16/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedPurchaseQuotationCell: UITableViewCell {

    @IBOutlet weak var productNameLabel: UILabel!
    
    
    @IBOutlet weak var requestedQty: UILabel!
    
    @IBOutlet weak var requestedUnitPrice: UILabel!
    
    @IBOutlet weak var storeUrlButton: UIButton!
    
    @IBOutlet weak var itemUrlButton: UIButton!
    
    @IBOutlet weak var customerName: UILabel!
    
    @IBOutlet weak var customerEmail: UILabel!
    
    @IBOutlet weak var downloadButton: UIButton!
    
    @IBOutlet weak var quantity: UITextField!
    
    @IBOutlet weak var unitPrice: UITextField!
    
    /*-------Invoice cell-------*/
    
    @IBOutlet weak var originTextField: UITextField!
    
    @IBOutlet weak var qualityTextField: UITextField!
    
    @IBOutlet weak var packingTextField: UITextField!
    
    @IBOutlet weak var validityTextField: UITextField!
    
    @IBOutlet weak var remarksTextField: UITextField!
    
    @IBOutlet weak var invoiceCellView: UIView!
    
    /*-----Comment Cell------*/
    
    @IBOutlet weak var commentTextView: UITextView!
    
    @IBOutlet weak var commentCellView: UIView!
    /*------Chat history cell-----*/
    
    /*------Purchase Url cell-----*/
    
    @IBOutlet weak var productSku: UITextField!
    
    @IBOutlet weak var agreedQty: UITextField!
    
    @IBOutlet weak var agreedUnitPrice: UITextField!
    
    
    @IBOutlet weak var quotationCellView: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
