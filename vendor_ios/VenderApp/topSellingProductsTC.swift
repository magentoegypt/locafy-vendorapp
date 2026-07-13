//
//  dashboardProductsTC.swift
//  VenderApp
//
//  Created by Cedcoss on 06/10/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class topSellingProductsTC: UITableViewCell {

    static let reuseId = "topSellingProductsTC"
    var prodData = [[String:String]]()
    var parent:ced_vendordashboard?
    
    lazy var topView:UIView = {
        let view = UIView()
        view.backgroundColor = DynamicColor.themeColor
        
        let img = UIImageView()
        img.contentMode = .scaleAspectFit
        img.backgroundColor = .clear
        img.image = UIImage(named: "latestProd")?.withRenderingMode(.alwaysTemplate)
        img.tintColor = .white
        
        let lbl = UILabel()
        lbl.text = "Top Selling Products".localized
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
        
    lazy var productCollection:UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection  = .horizontal
        layout.minimumInteritemSpacing = 0.0
        layout.minimumLineSpacing      = 0.0
        let collectionView      = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.register(topSellingProductsCC.self, forCellWithReuseIdentifier: topSellingProductsCC.reuseId)
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
        
        addSubview(topView)
        topView.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 0, paddingLeft: 8, paddingRight: 8, height: 45)
        
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
    
}
extension topSellingProductsTC:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return prodData.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: topSellingProductsCC.reuseId, for: indexPath) as! topSellingProductsCC
        cell.populate(with: prodData[indexPath.item])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return productCollection.frame.size
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let  pageWidth = self.productCollection.frame.size.width;
        self.pageControl.currentPage = Int(self.productCollection.contentOffset.x / pageWidth)
    }
    
}


class topSellingProductsCC:UICollectionViewCell{
    static let reuseId = "topSellingProductsCC"
    
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
        createRow(for: "Product Name".localized, value: data["product_name"])
        createRow(for: "Product Price".localized, value: data["vendor_price"])
        createRow(for: "Qty".localized, value: data["qty_ordered"])
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
