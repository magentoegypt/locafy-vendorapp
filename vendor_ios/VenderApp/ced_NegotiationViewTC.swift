//
//  ced_NegotiationViewTC.swift
//  VenderApp
//
//  Created by cedcoss on 07/07/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_NegotiationViewTC: UITableViewCell {
    static var reuseID = "ced_NegotiationViewTC"
    lazy var quotDetailHeading:UILabel = {
        let label = UILabel()
        label.text = "Negotiation Details"
        label.font = .systemFont(ofSize: 16, weight: .semibold)
        label.textAlignment = .center
        return label
    }()
    lazy var ProductRequestName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = "Product Requested"
        return label
    }()
    lazy var ProductRequestValue:UIButton = {
        let btn = UIButton()
        btn.titleLabel?.font = .systemFont(ofSize: 14, weight: .regular)
        btn.setTitleColor(DynamicColor.labelColor, for: .normal)
        btn.layer.cornerRadius = 2.0
        btn.layer.borderWidth = 1.0
        btn.layer.borderColor = UIColor.init(hexString: "#f2f2f2")?.cgColor
        
        return btn
    }()
    lazy var ProductRequestStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [ProductRequestName,ProductRequestValue])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
    lazy var QuantityName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = "Quantity"
        return label
    }()
    lazy var QuantityValue:UITextField = {
        let field = UITextField()
        field.font = .systemFont(ofSize: 14, weight: .regular)
        field.borderStyle = .roundedRect
        return field
    }()
    lazy var QuantityStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [QuantityName,QuantityValue])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
   
    lazy var priceName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = "Price"
        return label
    }()
    lazy var priceValue:UITextField = {
        let field = UITextField()
        field.font = .systemFont(ofSize: 14, weight: .regular)
        field.borderStyle = .roundedRect
        return field
    }()
    lazy var priceStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [priceName,priceValue])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
    
    lazy var mainStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [ProductRequestStack,QuantityStack,priceStack])
        stack.spacing = 2.0
        stack.alignment = .fill
        stack.distribution = .fillProportionally
        stack.axis = .vertical
        return stack
    }()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func configureView(){
        backgroundColor = DynamicColor.ViewBackgroundColor
        addSubview(quotDetailHeading)
        quotDetailHeading.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingRight: 5,  height: 40)
        addSubview(mainStack)
        mainStack.anchor(top: quotDetailHeading.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
    }
    func populateData(data: NegotiationDetail){
        self.priceValue.text = data.nprice
        self.ProductRequestValue.setTitle("Please Select Product", for: .normal)//= data.product_name
        self.QuantityValue.text = data.nqty
        
    }
}
