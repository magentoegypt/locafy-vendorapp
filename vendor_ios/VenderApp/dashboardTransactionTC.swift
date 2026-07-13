//
//  dashboardProductsTC.swift
//  VenderApp
//
//  Created by Cedcoss on 06/10/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class dashboardTransactionTC: UITableViewCell {

    static let reuseId = "dashboardTransactionTC"
    var prodData = [[String:String]]()
    var parent:ced_vendordashboard?
    
    lazy var topView:UIView = {
        let view = UIView()
        view.backgroundColor = DynamicColor.themeColor
        
        let img = UIImageView()
        img.contentMode = .scaleAspectFit
        img.backgroundColor = .clear
        img.image = UIImage(named: "transaction")?.withRenderingMode(.alwaysTemplate)
        img.tintColor = .white
        
        let lbl = UILabel()
        lbl.text = "LatestTransaction".localized
        lbl.font = .systemFont(ofSize: 15, weight: .semibold)
        lbl.textColor = .white
        
        view.addSubview(img)
        img.anchor( left: view.leadingAnchor, paddingLeft: 8, width: 35, height: 35)
        img.centerY(inView: view)
        
        view.addSubview(lbl)
        lbl.anchor( left: img.trailingAnchor, paddingLeft: 16, height: 40)
        lbl.centerY(inView: view)
        
        return view
    }()
    
    lazy var viewAll:UIButton = {
        let button = UIButton()
        button.setTitle("View All".localized, for: .normal)
        button.setTitleColor(DynamicColor.themeColor, for: .normal)
        button.titleLabel?.font = .systemFont(ofSize: 13, weight: .semibold)
        button.backgroundColor = .clear
        button.addTarget(self, action: #selector(viewAllTapped), for: .touchUpInside)
        return button
    }()
    
    lazy var productCollection:UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection  = .horizontal
        layout.minimumInteritemSpacing = 0.0
        layout.minimumLineSpacing      = 0.0
        let collectionView      = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.register(dashboardTransactionCC.self, forCellWithReuseIdentifier: dashboardTransactionCC.reuseId)
        collectionView.delegate         = self
        collectionView.dataSource       = self
        collectionView.backgroundColor  = .white
        collectionView.isPagingEnabled  = true
        collectionView.showsHorizontalScrollIndicator = false
        return collectionView
    }()
    
    lazy var pageControl: UIPageControl = {
        let control = UIPageControl()
        control.numberOfPages           = 3
        control.currentPage             = 0
        control.pageIndicatorTintColor = .white
        control.currentPageIndicatorTintColor = .black
        if #available(iOS 13.0, *) {
            control.backgroundColor = .systemGray5
        } else {
            control.backgroundColor = DynamicColor.secondaryViewBackground
        }
        control.clipsToBounds = true
        control.layer.cornerRadius = 10
        control.isEnabled = false
        return control
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none
        contentView.isMultipleTouchEnabled = true
        setupSubview()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func setupSubview(){
        addSubview(viewAll)
        viewAll.anchor(top: topAnchor,right: trailingAnchor, paddingTop: 0, paddingRight: 8, height: 45)
        
        addSubview(topView)
        topView.anchor(top: topAnchor, left: leadingAnchor, right: viewAll.leadingAnchor, paddingTop: 0, paddingLeft: 8, paddingRight: 8, height: 45)
        
        addSubview(productCollection)
        productCollection.anchor(top: topView.bottomAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 8, paddingLeft: 16, paddingRight: 16)
    
        addSubview(pageControl)
        pageControl.anchor(top: productCollection.bottomAnchor,bottom: bottomAnchor, paddingTop: 8, paddingBottom: 8)
        pageControl.centerX(inView: self)
    }
    
    func setData(data: [[String:String]]){
        prodData = data
        pageControl.numberOfPages = data.count
        productCollection.reloadData()
    }
    
    @objc func viewAllTapped(){
        let storybordOrder = UIStoryboard(name: "Main", bundle: nil)
        let viewControl = storybordOrder.instantiateViewController(withIdentifier: "viewtransaction") as! ced_viewTransactions
        parent?.navigationController?.pushViewController(viewControl, animated: true)
    }
    
}
extension dashboardTransactionTC:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return prodData.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: dashboardTransactionCC.reuseId, for: indexPath) as! dashboardTransactionCC
        cell.populate(with: prodData[indexPath.item])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return productCollection.frame.size
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if  let transaction = prodData[indexPath.item]["payment_id"]{
            let view = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "viewTansactView") as! ced_viewTransactionView
            view.transactionId = transaction
            parent?.navigationController?.pushViewController(view, animated: true)
        }
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let  pageWidth = self.productCollection.frame.size.width;
        self.pageControl.currentPage = Int(self.productCollection.contentOffset.x / pageWidth)
    }
    
}


class dashboardTransactionCC:UICollectionViewCell{
    static let reuseId = "dashboardTransactionCC"
    
    lazy var container:UIView = {
        let view = UIView()
        view.backgroundColor = DynamicColor.secondaryViewBackground
        return view
    }()
    
    lazy var vStaxk:UIStackView = {
        let view = UIStackView()
        view.axis = .vertical
        view.distribution = .fillEqually
        view.spacing = 5
        return view
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        addSubview(container)
        container.anchor(top: topAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
        container.addSubview(vStaxk)
        vStaxk.anchor(top: container.topAnchor, left: container.leadingAnchor, bottom: container.bottomAnchor, right: container.trailingAnchor, paddingTop: 5, paddingLeft: 8, paddingBottom: 5, paddingRight: 8)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func populate(with data:[String:String]){
        vStaxk.subviews.forEach{$0.removeFromSuperview()}
        container.cardView()
        
        createRow(for: "Transaction ID".localized, value: data["transaction_id"])
        createRow(for: "paymentid".localized, value: data["payment_id"])
        createRow(for: "Amount".localized, value: data["amount"])
        createRow(for: "Payment Mode".localized, value: data["payment_mode"])
        createRow(for: "Created At".localized, value: data["created_at"])
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
        
        vStaxk.addArrangedSubview(stack)
    }
    
}
