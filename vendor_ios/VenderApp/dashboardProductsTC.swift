//
//  dashboardProductsTC.swift
//  VenderApp
//
//  Created by Cedcoss on 06/10/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class dashboardProductsTC: UITableViewCell {

    static let reuseId = "dashboardProductsTC"
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
        lbl.text = "Latest Products".localized
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
        collectionView.register(dashboardProductsCC.self, forCellWithReuseIdentifier: dashboardProductsCC.reuseId)
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
        let storybordOrder = UIStoryboard(name: "ProductAddon", bundle: nil)
        let viewControl = storybordOrder.instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
        parent?.navigationController?.pushViewController(viewControl, animated: true)
    }
    
}
extension dashboardProductsTC:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return prodData.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: dashboardProductsCC.reuseId, for: indexPath) as! dashboardProductsCC
        cell.populate(with: prodData[indexPath.item])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return productCollection.frame.size
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if let parentController = parent,let prodId = prodData[indexPath.item]["product_id"] {
            Alert_File.addLoadingIndicator(parentController, msg: "Loading Please Wait...".localized)
            
            let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary;
            let vendor_id = userData["vendorId"] as! String;
            let hashkey    = userData["hashKey"] as! String;
            let storeId = UserDefaults.standard.value(forKey: "storeId") as? String ?? ""
            
            var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
            postString += "&product_id="+prodId
            postString += "&store_id=" + storeId
            
            print(postString);
            parentController.productId = prodId
            parentController.sendRequest(url: "vproductapi/vproducts/info/", parameters: postString);
        }
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let  pageWidth = self.productCollection.frame.size.width;
        self.pageControl.currentPage = Int(self.productCollection.contentOffset.x / pageWidth)
    }
    
}


class dashboardProductsCC:UICollectionViewCell{
    static let reuseId = "dashboardProductsCC"
    
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
        createRow(for: "Product ID".localized, value: data["product_id"])
        createRow(for: "Product Status".localized, value: data["product_status"])
        createRow(for: "Product Price".localized, value: data["vendor_price"])
        createRow(for: "Product Qty".localized, value: data["product_qty"])
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
