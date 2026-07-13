//
//  ced_Quote&AccountInformationCELL.swift
//  VenderApp
//
//  Created by Macmini on 15/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_Quote_AccountInformationCELL: UITableViewCell {
    
    
    @IBOutlet weak var ced_Quoteview: UIView!
    
    
    @IBOutlet weak var AccountInformationcell: UIView!
    
    
    @IBOutlet weak var AddressAndShippingInformationcell: UIView!
    
    @IBOutlet weak var ShippingInformationCell: UIView!
    
    
    @IBOutlet weak var PODateLbl: UILabel!
    @IBOutlet weak var postatusLbl: UILabel!
    
    
    
    @IBOutlet weak var headerlable: UILabel!
    @IBOutlet weak var toplable_order: UILabel!
    @IBOutlet weak var QuoteDate: UILabel!
    @IBOutlet weak var CurrentQuoteStatus: UILabel!
    @IBOutlet weak var QuoteCreatedFrom: UILabel!
    @IBOutlet weak var QuotedTotalQuantity: UILabel!
    @IBOutlet weak var QuotedTotalPrice: UILabel!
    
    @IBOutlet weak var quoteDataLbl: UILabel!
    @IBOutlet weak var currentQuoteLvl: UILabel!
    @IBOutlet weak var quoteCreatedFromLbl: UILabel!
    
    
    //    MARK: Account info cell outlet
    @IBOutlet weak var AccountInformation_Lable: UILabel!
    @IBOutlet weak var CustomerName: UILabel!
    @IBOutlet weak var Email: UILabel!
    @IBOutlet weak var CustomerGroup: UILabel!
    
    @IBOutlet weak var customerNameLbl: UILabel!
    @IBOutlet weak var EmailLbl: UILabel!
    
    @IBOutlet weak var customerGroupLbl: UILabel!
    
    //    MARK: ADdressShiping
    @IBOutlet weak var AddressAndShippingInformation: UILabel!
    @IBOutlet weak var ShippingAddress: UILabel!
    
    @IBOutlet weak var subadd1: UILabel!
    @IBOutlet weak var subadd2: UILabel!
    @IBOutlet weak var subadd3: UILabel!
    @IBOutlet weak var subadd4: UILabel!
    @IBOutlet weak var subadd5: UILabel!
    
    //    MARK: Shipping Information
    @IBOutlet weak var ShippingInformation: UILabel!
    @IBOutlet weak var CustomerNameShopinginfo: UILabel!
    @IBOutlet weak var emailshopinginfo: UILabel!
    
    
    
    
    
    /////contentview
    
    
    @IBOutlet weak var cellview: UIView!
    
    @IBOutlet weak var cellview1: UIView!
    
    
    @IBOutlet weak var cellview2: UIView!
    
    @IBOutlet weak var cellview4: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }

}
