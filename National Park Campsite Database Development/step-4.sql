-- select the park name, campground name, open_from_mm, open_to_mm & daily_fee ordered by park name and then campground name
-- (expected: 7 rows, starting with "Blackwoods")

SELECT park.name, campground.name, open_from_mm, open_to_mm, daily_fee
FROM park
JOIN campground ON park.park_id =campground.park_id
ORDER BY park.name, campground.name;

-- select the park name and the total number of campgrounds for each park ordered by park name
-- (expected: 3 rows, starting with "Acadia")

SELECT park.name, COUNT(campground_id)
FROM park
JOIN campground ON park.park_id = campground.park_id
GROUP BY park.name
ORDER BY park.name;

-- select the park name, campground name, site number, max occupancy, accessible, max rv length, utilities where the campground name is 'Blackwoods'
-- (expected: 12 rows)

SELECT park.name, campground.name, site_number, max_occupancy, accessible,max_rv_length,utilities
FROM park
JOIN campground ON park.park_id = campground.park_id
JOIN site ON campground.campground_id = site.campground_id
WHERE campground.name = 'Blackwoods';

-- select site number, reservation name, reservation from and to date ordered by reservation from date
-- (expected: 44 rows, starting with the earliest date)


SELECT site_number, reservation.name, from_date, to_date
FROM site
JOIN reservation ON site.site_id = reservation.site_id
ORDER BY from_date;
