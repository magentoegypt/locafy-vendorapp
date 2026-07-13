//
//  newPaymentreportTc.swift
//  VenderApp
//
//  Created by Cedcoss on 08/09/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class newPaymentreportTc: UITableViewCell {

    static let reuseId = "newPaymentreportTc"
    var parentController:UIViewController?
    
    lazy var container:UIView = {
        let view = UIView()
        view.clipsToBounds = true
        view.layer.borderWidth = 1.5
        view.layer.borderColor = DynamicColor.themeColor.cgColor
        view.layer.cornerRadius = 10.0
        view.backgroundColor = DynamicColor.tertiaryViewBackground
        return view
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
        addSubview(container)
        container.anchor(top: topAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
        
        
        container.addSubview(totalStack)
        totalStack.anchor(top: container.topAnchor, left: container.leadingAnchor, bottom: container.bottomAnchor, right: container.trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
    }
    
    func populate(with data:[String:String]?,payment_status:[String:String]?){
        totalStack.subviews.forEach{$0.removeFromSuperview()}
        
        guard let prodData = data else {return}
        guard let payment_status = payment_status else {return}
        
        createRow(for: "Order Description".localized, value: prodData["Order Description"]?.html2String)
        createRow(for: "Transaction Date".localized, value: prodData["Transaction Date"]?.dateConvertToCurrentTimezone("yyyy-MM-dd HH:mm:ss"))
        createRow(for: "Transaction ID".localized, value: prodData["Transaction Id"])
        createRow(for: "Credit Amount".localized, value: prodData["Amount"])
        createRow(for: "Status".localized, value: payment_status[prodData["Status"] ?? ""])
    }
    
    
    
    private func createRow(for heading: String?, value: String?) {
        let label = UILabel()
        label.text = heading
        label.font = .systemFont(ofSize: 15, weight: .semibold)
        label.numberOfLines = 0
        
        label.anchor(width:150)
        
        let labelTwo = UILabel()
        labelTwo.text = value
        labelTwo.numberOfLines = 0
        labelTwo.font = .systemFont(ofSize: 15, weight: .regular)
        labelTwo.textColor = DynamicColor.labelColor
        
        let stack = UIStackView(arrangedSubviews: [label,labelTwo])
        stack.axis = .horizontal
//        stack.distribution = .fillEqually
        stack.spacing = 8.0
//        stack.anchor(height: 18)
        
        totalStack.addArrangedSubview(stack)
    }
    

}
extension Data {
  var html2AttributedString: NSAttributedString? {
    do {
      return try NSAttributedString(data: self, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding: String.Encoding.utf8.rawValue], documentAttributes: nil)
    } catch {
      print("error:", error)
      return  nil
    }
  }
  var html2String: String {
    return html2AttributedString?.string ?? ""
  }
    
}

extension String {
  var html2AttributedString: NSMutableAttributedString? {
    return Data(utf8).normalAttributedString
  }
  var html2String: String {
    return html2AttributedString?.string ?? ""
  }
}
extension Data{
    var normalAttributedString: NSMutableAttributedString? {
        do {
            
            
            let string = try NSMutableAttributedString(data: self, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding: String.Encoding.utf8.rawValue], documentAttributes: nil)
            if #available(iOS 13.0, *) {
                string.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.systemGray, range: NSMakeRange(0, string.length))
            } else {
                string.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.black, range: NSMakeRange(0, string.length))
            }
            if(UIDevice.current.userInterfaceIdiom == .pad){
                string.addAttribute(NSAttributedString.Key.font, value: UIFont.systemFont(ofSize: 18, weight: .regular), range: NSMakeRange(0, string.length))
            }
            else{
                string.addAttribute(NSAttributedString.Key.font, value: UIFont.systemFont(ofSize: 14, weight: .regular), range: NSMakeRange(0, string.length))
            }
            return string;
            
        } catch {
            print("error:", error)
            return  nil
        }
    }
}
