//
//  ced_newViewTransactTableViewCell.swift
//  VenderApp
//
//  Created by Cedcoss on 31/01/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_newViewTransactTableViewCell: UITableViewCell {
    
    static let reuseId = "ced_newViewTransactTableViewCell"

    lazy var container:UIView = {
        let view = UIView()
        view.backgroundColor = DynamicColor.tertiaryViewBackground
//        view.layer.cornerRadius = 5.0
//        view.layer.borderColor = Settings.secondaryColor.cgColor
//        view.layer.borderWidth = 1.0
        view.cardView()
        return view
    }()
    
   
    lazy var totalStack:UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
//        stack.distribution = .fillProportionally
        stack.spacing = 7.0
        
        return stack
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        contentView.isMultipleTouchEnabled = true
        selectionStyle = .none
        addSubview(container)
        container.anchor(top: topAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor,
                         paddingTop: 8, paddingLeft: 16, paddingBottom: 8,paddingRight: 16)
        
        container.addSubview(totalStack)
        totalStack.anchor(top: container.topAnchor, left: container.leadingAnchor ,bottom: container.bottomAnchor,right: container.trailingAnchor, paddingTop: 8, paddingLeft: 8,paddingBottom: 8,paddingRight: 8)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func populate(with total: [String:String]?) {
        totalStack.subviews.forEach({ $0.removeFromSuperview() })
        guard let total = total else {return}
        createRow(for: "Created At".localized, value: total["created_at"])
        createRow(for: "Transaction ID".localized, value: total["transaction_id"])
        createRow(for: "Amount".localized, value: total["amount"])
      //  createRow(for: "Adjustment Amount".localized, value: total["adjustment_amount"])
        createRow(for: "Net Amount".localized, value: total["net_amount"])
      //  createRow(for: "Payment Mode".localized, value: total["payment_mode"])
        
        if let amount_desc = total["amount_desc"]{

            createRow(for: "Amount Description".localized, value: amount_desc)
            
        }
        
    }
    
    func populateData(with order_detail:[String:String]?){
        totalStack.subviews.forEach({ $0.removeFromSuperview() })
        guard let order_detail = order_detail else {return}
        createRow(for: "Order Id".localized, value: order_detail["order_id"])
        createRow(for: "Grand Total".localized, value: order_detail["vendor_payment"])
       // createRow(for: "Commission Fee", value: order_detail["commission_fee"])
        createRow(for: "Payment Mode".localized, value: order_detail["order_paymode"])
        createRow(for: "Total Payment".localized, value: order_detail["vendor_payment"])
    }
    
    func singlepopulateData(with order_detail:[String:String]?){
        totalStack.subviews.forEach({ $0.removeFromSuperview() })
        guard let order_detail = order_detail else {return}
        createRow(for: "Order Id".localized, value: order_detail["order_id"])
        createRow(for: "Grand Total".localized, value: order_detail["vendor_payment"])
       // createRow(for: "Commission Fee", value: order_detail["commission_fee"])
        createRow(for: "Single Payment Mode".localized, value: order_detail["order_paymode"])
        createRow(for: "Total Payment".localized, value: order_detail["vendor_payment"])
    }
    
    private func createRow(for heading: String?, value: String?) {
        let label = UILabel()
        label.text = heading
        label.numberOfLines = 0
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.textColor = DynamicColor.labelColor
        label.anchor(width:150)

        let labelTwo = UILabel()
        labelTwo.text = value
        labelTwo.font = .systemFont(ofSize: 14, weight: .regular)
        labelTwo.textColor = DynamicColor.labelColor
        labelTwo.numberOfLines = 0

        let stack = UIStackView(arrangedSubviews: [label,labelTwo])
        stack.axis = .horizontal
        stack.distribution = .fill
        stack.spacing = 8.0

        totalStack.addArrangedSubview(stack)

    }
    
}
