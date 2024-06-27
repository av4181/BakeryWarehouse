-- Supplier
INSERT INTO supplier (name, contactPerson, email, phoneNumber) VALUES
('Acme Ingredients', 'Alice Smith', 'alice.smith@acme.com', '+1-555-123-4567'),
('Quality Foods Co.', 'Bob Johnson', 'bob.johnson@qualityfoods.com', '+1-555-987-6543'),
('Fresh Farms', 'Carol Brown', 'carol.brown@freshfarms.com', '+1-555-543-2109'),
('Global Grains', 'David Lee', 'david.lee@globalgrains.com', '+1-555-876-5432'),
('Sweet Treats Inc.', 'Eva Davis', 'eva.davis@sweettreats.com', '+1-555-210-9876'),
('Supplier 1', 'Alice Smith', 'alice.smith@supplier1.com', '+1-555-111-1111'),
('Supplier 2', 'Bob Johnson', 'bob.johnson@supplier2.com', '+1-555-222-2222'),
('Supplier 3', 'Carol Brown', 'carol.brown@supplier3.com', '+1-555-333-3333'),
('Supplier 4', 'David Lee', 'david.lee@supplier4.com', '+1-555-444-4444'),
('Supplier 5', 'Eva Davis', 'eva.davis@supplier5.com', '+1-555-555-5555'),
('Supplier 6', 'Frank Miller', 'frank.miller@supplier6.com', '+1-555-666-6666'),
('Supplier 7', 'Grace Wilson', 'grace.wilson@supplier7.com', '+1-555-777-7777'),
('Supplier 8', 'Henry Taylor', 'henry.taylor@supplier8.com', '+1-555-888-8888'),
('Supplier 9', 'Ivy Anderson', 'ivy.anderson@supplier9.com', '+1-555-999-9999'),
('Supplier 10', 'Jack Thomas', 'jack.thomas@supplier10.com', '+1-555-101-0101'),
('Supplier 11', 'Katie White', 'katie.white@supplier11.com', '+1-555-112-1212'),
('Supplier 12', 'Liam Harris', 'liam.harris@supplier12.com', '+1-555-123-1313'),
('Supplier 13', 'Mia Martin', 'mia.martin@supplier13.com', '+1-555-134-1414'),
('Supplier 14', 'Noah Clark', 'noah.clark@supplier14.com', '+1-555-145-1515'),
('Supplier 15', 'Olivia Hall', 'olivia.hall@supplier15.com', '+1-555-156-1616'),
('Supplier 16', 'Peter Young', 'peter.young@supplier16.com', '+1-555-167-1717'),
('Supplier 17', 'Quinn Adams', 'quinn.adams@supplier17.com', '+1-555-178-1818'),
('Supplier 18', 'Riley Baker', 'riley.baker@supplier18.com', '+1-555-189-1919'),
('Supplier 19', 'Sophia Green', 'sophia.green@supplier19.com', '+1-555-190-2020'),
('Supplier 20', 'Ethan King', 'ethan.king@supplier20.com', '+1-555-201-2121');

-- Ingredient
INSERT INTO ingredient (ingredientNumber, name, stock, status, supplierId, expirationDate) VALUES
('ING-001', 'Flour', 100, 'AVAILABLE', 1, '2024-06-03'),
('ING-002', 'Sugar', 50, 'AVAILABLE', 2, '2025-01-01'),
('ING-003', 'Eggs', 20, 'AVAILABLE', 3, '2024-06-04'),
('ING-004', 'Butter', 30, 'OUT_OF_STOCK', 4, '2024-06-03'),
('ING-005', 'Chocolate Chips', 15, 'AVAILABLE', 5, '2024-12-01'),
('ING-006', 'Flour', 5, 'AVAILABLE', 1, CURRENT_DATE + INTERVAL '2 days'),
('ING-008', 'Sugar', 5, 'AVAILABLE', 2, CURRENT_DATE + INTERVAL '3 days'),
('ING-009', 'Eggs', 5, 'AVAILABLE', 3, CURRENT_DATE + INTERVAL '1 day'),
('ING-010', 'Butter', 5, 'AVAILABLE', 4, CURRENT_DATE + INTERVAL '5 days'),
('ING-011', 'Chocolate Chips', 5, 'AVAILABLE', 5, CURRENT_DATE + INTERVAL '7 days'),
('ING-012', 'Vanilla Extract', 30, 'AVAILABLE', 1, CURRENT_DATE + INTERVAL '1 month'),
('ING-013', 'Baking Powder', 45, 'AVAILABLE', 2, CURRENT_DATE + INTERVAL '6 months'),
('ING-014', 'Salt', 60, 'AVAILABLE', 3, CURRENT_DATE + INTERVAL '1 year'),
('ING-015', 'Baking Soda', 25, 'AVAILABLE', 4, CURRENT_DATE + INTERVAL '4 days'),
('ING-016', 'Cocoa Powder', 18, 'AVAILABLE', 5, CURRENT_DATE + INTERVAL '9 days'),
('ING-017', 'Milk', 5, 'AVAILABLE', 1, CURRENT_DATE + INTERVAL '11 days'),
('ING-018', 'Cream Cheese', 35, 'AVAILABLE', 2, CURRENT_DATE + INTERVAL '2 weeks'),
('ING-019', 'Strawberries', 50, 'AVAILABLE', 3, CURRENT_DATE + INTERVAL '3 weeks'),
('ING-020', 'Blueberries', 42, 'AVAILABLE', 4, CURRENT_DATE + INTERVAL '1 month'),
('ING-021', 'Raspberries', 28, 'AVAILABLE', 5, CURRENT_DATE + INTERVAL '2 months'),
('ING-022', 'Pecans', 15, 'AVAILABLE', 1, CURRENT_DATE + INTERVAL '3 months'),
('ING-023', 'Walnuts', 22, 'AVAILABLE', 2, CURRENT_DATE + INTERVAL '4 months'),
('ING-024', 'Almonds', 38, 'AVAILABLE', 3, CURRENT_DATE + INTERVAL '5 months'),
('ING-025', 'Hazelnuts', 12, 'AVAILABLE', 4, CURRENT_DATE + INTERVAL '6 months'),
('ING-026', 'Pistachios', 10, 'AVAILABLE', 5, CURRENT_DATE + INTERVAL '1 year');
-- Order
INSERT INTO orders (id, orderdate, status) VALUES
(gen_random_uuid(), NOW(), 'PENDING'),
(gen_random_uuid(), NOW() - INTERVAL '1 day', 'FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '2 days', 'FAILED'),
(gen_random_uuid(), NOW() - INTERVAL '3 days', 'CANCELLED'),
(gen_random_uuid(), NOW() - INTERVAL '4 days', 'PARTIALLY_FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '5 day', 'FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '6 days', 'FAILED'),
(gen_random_uuid(), NOW() - INTERVAL '7 days', 'CANCELLED'),
(gen_random_uuid(), NOW() - INTERVAL '8 days', 'PARTIALLY_FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '9 day', 'FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '10 days', 'FAILED'),
(gen_random_uuid(), NOW() - INTERVAL '11 days', 'CANCELLED'),
(gen_random_uuid(), NOW() - INTERVAL '12 days', 'PARTIALLY_FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '13 day', 'FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '14 days', 'FAILED'),
(gen_random_uuid(), NOW() - INTERVAL '15 days', 'CANCELLED'),
(gen_random_uuid(), NOW() - INTERVAL '16 days', 'PARTIALLY_FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '17 day', 'FULFILLED'),
(gen_random_uuid(), NOW() - INTERVAL '18 days', 'FAILED'),
(gen_random_uuid(), NOW() - INTERVAL '19 days', 'CANCELLED'),
(gen_random_uuid(), NOW() - INTERVAL '20 days', 'PARTIALLY_FULFILLED');


-- Order_Item
INSERT INTO orderItem (orderId, ingredientNumber, quantity) VALUES
                                                                ((SELECT id FROM orders LIMIT 1), 'ING-001', 10),
    ((SELECT id FROM orders LIMIT 1), 'ING-002', 5),
    ((SELECT id FROM orders OFFSET 1 LIMIT 1), 'ING-003', 3),
    ((SELECT id FROM orders OFFSET 2 LIMIT 1), 'ING-004', 8),
    ((SELECT id FROM orders OFFSET 3 LIMIT 1), 'ING-005', 2);

-- Delivery
INSERT INTO delivery (ordernumber,destination, deliveryDate, status) VALUES
                                                                         ((SELECT id FROM orders LIMIT 1),'Bakery A',NOW(), 'SUCCESS'),
    ((SELECT id FROM orders OFFSET 1 LIMIT 1),'Bakery B', NOW() - INTERVAL '1 day', 'SUCCESS'),
    ((SELECT id FROM orders OFFSET 2 LIMIT 1), 'Bakery C', NOW() - INTERVAL '2 days', 'FAILED'),
    ((SELECT id FROM orders OFFSET 3 LIMIT 1),null, NOW() - INTERVAL '3 days', 'FAILED'),
    ((SELECT id FROM orders OFFSET 4 LIMIT 1),'Bakery D', NOW() - INTERVAL '4 days', 'SUCCESS');

-- Delivery_item
INSERT INTO delivery_item (delivery_id, ingredientNumber, quantity) VALUES
                                                                        ((SELECT id FROM delivery LIMIT 1), 'ING-001', 10),
    ((SELECT id FROM delivery LIMIT 1), 'ING-002', 5),
    ((SELECT id FROM delivery OFFSET 1 LIMIT 1), 'ING-003', 3),
    ((SELECT id FROM delivery OFFSET 4 LIMIT 1), 'ING-004', 5),
    ((SELECT id FROM delivery OFFSET 4 LIMIT 1), 'ING-005', 2);