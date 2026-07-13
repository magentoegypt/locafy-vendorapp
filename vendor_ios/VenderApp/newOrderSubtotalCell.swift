//
//  newOrderSubtotalCell.swift
//  VenderApp
//
//  Created by Cedcoss on 20/07/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class newOrderSubtotalCell: UITableViewCell {
    
    static let reuseId = "newOrderSubtotalCell"
    
    lazy var container:UIView = {
        let view = UIView()
        view.clipsToBounds = true
        view.layer.borderWidth = 1.5
        view.layer.borderColor = DynamicColor.themeColor.cgColor
        view.layer.cornerRadius = 10.0
        view.backgroundColor = DynamicColor.tertiaryViewBackground
        return view
    }()
    
    lazy var topTotalLabel:UILabel = {
        let lbl = UILabel()
        lbl.textColor = DynamicColor.ViewBackgroundColor
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.font = .systemFont(ofSize: 17, weight: .semibold)
        lbl.textAlignment = .center
        lbl.clipsToBounds = true
        lbl.layer.cornerRadius = 6.0
        lbl.text = "Order Total".localized
        return lbl
    }()
    
    lazy var totalStack:UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.spacing = 5.0
        return stack
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none
        contentView.isMultipleTouchEnabled = true
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func setupUI(){
        
        addSubview(topTotalLabel)
        topTotalLabel.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 40, paddingRight: 40, height: 40)
        
        addSubview(container)
        container.anchor(top: topTotalLabel.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 10, paddingBottom: 5, paddingRight: 10)
        
        container.addSubview(totalStack)
        totalStack.anchor(top: container.topAnchor, left: container.leadingAnchor, bottom: container.bottomAnchor, right: container.trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
    }
    
    func populate(with totalData:[String:String]?){
        totalStack.subviews.forEach{$0.removeFromSuperview()}
        
        guard let prodData = totalData else {return}
        var net_earn = prodData["net_earn"] ?? ""
        if net_earn.contains("SAR"){
            net_earn = net_earn.replacingOccurrences(of: "SAR", with: "EGP")
        }else if net_earn.contains("ر.س.‏"){
            net_earn = net_earn.replacingOccurrences(of: "ر.س.‏", with: "ج.م.‏")
        }
//        var order_total_str = prodData["order_total"] ?? ""
//        var order_commission_str = prodData["order_commission"] ?? ""
//        order_total_str = order_total_str.replacingOccurrences(of: " ", with: "")
//        order_total_str = order_total_str.replacingOccurrences(of: ",", with: "")
//        order_total_str = order_total_str.replacingOccurrences(of: "SAR", with: "")
//        order_total_str = order_total_str.replacingOccurrences(of: "EGP", with: "")
//        order_total_str = order_total_str.replacingOccurrences(of: "ج.م.‏", with: "")
//        order_total_str = order_total_str.replacingOccurrences(of: "ر.س.‏", with: "")
//        
//        order_commission_str = order_commission_str.replacingOccurrences(of: " ", with: "")
//        order_commission_str = order_commission_str.replacingOccurrences(of: ",", with: "")
//        order_commission_str = order_commission_str.replacingOccurrences(of: "SAR", with: "")
//        order_commission_str = order_commission_str.replacingOccurrences(of: "EGP", with: "")
//        order_commission_str = order_commission_str.replacingOccurrences(of: "ج.م.‏", with: "")
//        order_commission_str = order_commission_str.replacingOccurrences(of: "ر.س.‏", with: "")
//        
//        let order_total_Val =  Double(order_total_str) ?? 0
//        let order_commission_Val =  Double(order_commission_str) ?? 0
//        var order_total = String(format: "%.2f", order_total_Val - order_commission_Val)
//        if net_earn.contains("EGP"){
//            order_total = "EGP \(order_total)"
//        }else if net_earn.contains("ج.م.‏"){
//            order_total = "\(order_total) ج.م.‏"
//        }
        //Grand Total (Earned)
//        if(prodData["statusLabel"] == "تم الإلغاء" || prodData["statusLabel"] == "Canceled"){
//            createRow(for: "Grand Total".localized, value: net_earn)
//        }else{
//            
//        }
        createRow(for: "Subtotal".localized, value: prodData["order_subtotal"])
        createRow(for: "Tax Amount".localized, value: prodData["order_tax"])
        createRow(for: "Grand Total".localized, value: prodData["grand_total_earned"])
     //   createRow(for: "Commission Fee".localized, value: prodData["order_commission"])
        createRow(for: "Market Fee".localized, value: prodData["marketplace_fees"])
        createRow(for: "Service Tax".localized, value: prodData["service_tax"])
        createRow(for: "Vendor Payment".localized, value: net_earn)
        
    }
    
    func populate(totalInvoiceCreateData:[String:String]?){
        totalStack.subviews.forEach{$0.removeFromSuperview()}
        topTotalLabel.text = "Invoice Total".localized
        guard let prodData = totalInvoiceCreateData else {return}
        
        if prodData["order_subtotal_excl"] != ""{
            createRow(for: "Order Subtotal(Excl. Tax)".localized, value: prodData["order_subtotal_excl"])
        }
        if prodData["order_subtotal_incl"] != ""{
            createRow(for: "Order Subtotal(Incl. Tax)".localized, value: prodData["order_subtotal_incl"])
        }
        if prodData["order_subtotal"] != ""{
            createRow(for: "Subtotal".localized, value: prodData["order_subtotal"])
        }
        if prodData["order_shipping_without_currency"] != "0"{
            createRow(for: "Shipping & Handling".localized, value: prodData["order_shipping"])
        }
        if prodData["order_tax"] != ""{
            createRow(for: "Tax".localized, value: prodData["order_tax"])
        }
        if prodData["order_total_excl"] != ""{
            createRow(for: "Grand Total(Excl. Tax)".localized, value: prodData["order_total_excl"])
        }
        if prodData["order_total_incl"] != ""{
            createRow(for: "Grand Total(Incl. Tax)".localized, value: prodData["order_total_incl"])
        }
        if prodData["order_total"] != ""{
            createRow(for: "Grand Total".localized, value: prodData["order_total"])
        }
       
       
    }
    
    func populate(invoiceViewData:[String:String]?){
        totalStack.subviews.forEach{$0.removeFromSuperview()}
        topTotalLabel.text = "Invoice Total".localized
        guard let prodData = invoiceViewData else {return}
        
        if prodData["order_subtotal_excl"] != ""{
            createRow(for: "Order Subtotal(Excl. Tax)".localized, value: prodData["order_subtotal_excl"])
        }
        if prodData["order_subtotal_incl"] != ""{
            createRow(for: "Order Subtotal(Incl. Tax)".localized, value: prodData["order_subtotal_incl"])
        }
        if prodData["order_subtotal"] != ""{
            createRow(for: "Subtotal".localized, value: prodData["order_subtotal"])
        }
        if prodData["shipment_handled"] == "vendor"{
            createRow(for: "Shipping & Handling".localized, value: prodData["order_shipping"])
        }
        if prodData["order_tax"] != ""{
            createRow(for: "Tax".localized, value: prodData["order_tax"])
        }
        if prodData["order_total_excl"] != ""{
            createRow(for: "Grand Total(Excl. Tax)".localized, value: prodData["order_total_excl"])
        }
        if prodData["order_total_incl"] != ""{
            createRow(for: "Grand Total(Incl. Tax)".localized, value: prodData["order_total_incl"])
        }
        if prodData["order_total"] != ""{
            createRow(for: "Grand Total".localized, value: prodData["order_total"])
        }
    }
    
    func populate(creditmemoViewData:[String:String]?){
        totalStack.subviews.forEach{$0.removeFromSuperview()}
        topTotalLabel.text = " Credit Memo Totals".localized
        guard let prodData = creditmemoViewData else {return}
        
        if prodData["order_subtotal_excl"] != ""{
            createRow(for: "Order Subtotal(Excl. Tax)".localized, value: prodData["order_subtotal_excl"])
        }
        if prodData["order_subtotal_incl"] != ""{
            createRow(for: "Order Subtotal(Incl. Tax)".localized, value: prodData["order_subtotal_incl"])
        }
        if prodData["order_subtotal"] != ""{
            createRow(for: "Grand Total".localized, value: prodData["order_subtotal"])
        }
        if prodData["marketplace_fees"] != ""{
            createRow(for: "Marketplace Fees".localized, value: prodData["marketplace_fees"])
        }
        if prodData["shipment_handled"] == "vendor"{
            createRow(for: "Shipping Refund".localized, value: prodData["shipping_refund"])
        }
        createRow(for: "Adjustment Refund".localized, value: prodData["adjustment_positive"])
        createRow(for: "Adjustment Fee".localized, value: prodData["adjustment_negative"])
//        if prodData["order_tax"] != ""{
//            createRow(for: "Tax".localized, value: prodData["order_tax"])
//        }
        if prodData["order_total_excl"] != ""{
            createRow(for: "Grand Total(Excl. Tax)".localized, value: prodData["order_total_excl"])
        }
        if prodData["order_total_incl"] != ""{
            createRow(for: "Grand Total(Incl. Tax)".localized, value: prodData["order_total_incl"])
        }
        if prodData["order_total"] != ""{
            createRow(for: "Subtotal".localized, value: prodData["order_total"])
        } 
    }
    
    private func createRow(for heading: String?, value: String?) {
        let label = UILabel()
        label.text = heading
        label.font = .systemFont(ofSize: 15, weight: .semibold)
        label.numberOfLines = 0
        
//        label.anchor(width:140)
        
        let labelTwo = UILabel()
        labelTwo.text = value
        labelTwo.numberOfLines = 0
        labelTwo.font = .systemFont(ofSize: 15, weight: .regular)
        labelTwo.textColor = DynamicColor.labelColor
        
        if heading == "Grand Total".localized{
            label.font = .systemFont(ofSize: 15, weight: .bold)
            labelTwo.font = .systemFont(ofSize: 15, weight: .bold)
        }
        
        let stack = UIStackView(arrangedSubviews: [label,labelTwo])
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        stack.spacing = 8.0
//        stack.anchor(height: 18)
        
        totalStack.addArrangedSubview(stack)
    }

}

extension String {
    
    func removeCharacters(from forbiddenChars: CharacterSet) -> String {
           let passed = self.unicodeScalars.filter { !forbiddenChars.contains($0) }
           return String(String.UnicodeScalarView(passed))
       }
    
    func removing(charactersOf string: String) -> String {
        let characterSet = CharacterSet(charactersIn: string)
        let components = self.components(separatedBy: characterSet)
        return components.joined(separator: "")
    }
}
