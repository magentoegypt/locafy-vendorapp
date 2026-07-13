//
//  ced_QuotationViewTC.swift
//  VenderApp
//
//  Created by cedcoss on 07/07/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_QuotationViewTC: UITableViewCell {
    static var reuseID = "ced_QuotationViewTC"
    lazy var quotDetailHeading:UILabel = {
        let label = UILabel()
        label.text = "Quotation Details"
        label.font = .systemFont(ofSize: 16, weight: .semibold)
        label.textAlignment = .center
        return label
    }()
    lazy var reqProductName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = "Requested Product Name"
        return label
    }()
    lazy var reqProductValue:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    lazy var reqProductStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [reqProductName,reqProductValue])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
    lazy var reqQuantityName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = "Requested Quantity"
        return label
    }()
    lazy var reqQuantityValue:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    lazy var reqQuantityStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [reqQuantityName,reqQuantityValue])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
    lazy var estimatedBudgetPerQtyName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = "Estimated Budget Per Qty"
        return label
    }()
    lazy var estimatedBudgetPerQtyValue:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    lazy var estimatedBudgetPerQtyStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [estimatedBudgetPerQtyName,estimatedBudgetPerQtyValue])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()

    lazy var commentName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = "Comment"
        return label
    }()
    lazy var commentValue:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14, weight: .regular)
        return label
    }()
    lazy var commentStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [commentName,commentValue])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
    lazy var documentName:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = "Document File"
        return label
    }()
    lazy var documentValue:UIButton = {
        let btn = UIButton()
        btn.titleLabel?.lineBreakMode = .byCharWrapping
        btn.titleLabel?.font = .systemFont(ofSize: 14, weight: .regular)
        btn.setTitleColor(.systemBlue, for: .normal)
        return btn
    }()
    lazy var documentStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [documentName,documentValue])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
    
    lazy var mainStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [reqProductStack,documentStack,reqQuantityStack,estimatedBudgetPerQtyStack,commentStack])
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
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        contentView.isMultipleTouchEnabled = true
        configureView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configureView(){
        backgroundColor = DynamicColor.ViewBackgroundColor
        addSubview(quotDetailHeading)
        quotDetailHeading.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingRight: 5,  height: 40)
        addSubview(mainStack)
        mainStack.anchor(top: quotDetailHeading.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
    }
    func populateData(data: QuotationDetail){
        self.commentValue.text = data.comment
        self.reqProductValue.text = data.product_name
        self.documentValue.setTitle(data.documents_file, for: .normal)
        self.documentValue.titleLabel?.numberOfLines = 0
        self.reqQuantityValue.text = data.requested_quantity
        self.estimatedBudgetPerQtyValue.text = data.estimated_quantity
    }
}
